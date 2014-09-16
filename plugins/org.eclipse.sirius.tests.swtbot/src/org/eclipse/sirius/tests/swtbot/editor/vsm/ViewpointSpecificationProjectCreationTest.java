/*******************************************************************************
 * Copyright (c) 2010, 2014 THALES GLOBAL SERVICES.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.tests.swtbot.editor.vsm;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.sirius.business.internal.migration.description.VSMMigrationService;
import org.eclipse.sirius.business.internal.migration.description.VSMVersionSAXParser;
import org.eclipse.sirius.tests.support.api.TestsUtil;
import org.eclipse.sirius.tests.swtbot.support.api.AbstractSiriusSwtBotGefTestCase;
import org.eclipse.sirius.tests.swtbot.support.api.condition.ItemEnabledCondition;
import org.eclipse.sirius.tests.swtbot.support.utils.SWTBotUtils;
import org.eclipse.swtbot.eclipse.gef.finder.SWTGefBot;
import org.eclipse.swtbot.swt.finder.waits.Conditions;
import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.osgi.framework.Version;

/**
 * Tests that Sirius Specification Project is created properly.
 * 
 * @author mporhel
 */
public class ViewpointSpecificationProjectCreationTest extends AbstractSiriusSwtBotGefTestCase {

    private static final String VSP_SHOULD_CONTAIN = "The created VSM project should contain ";

    private static final String VSM_PROJECT_NAME = "org.eclipse.sirius.test.design";

    private static final String VSM = "test.odesign";

    private static final String WIZARD_FILE = "File";

    private static final String WIZARD_FINISH = "Finish";

    private static final String WIZARD_NEXT = "Next >";

    private static final String WIZARD_NEW = "New";

    private static final String WIZARD_PROJECT_NAME = "Project name:";

    private static final String WIZARD_VIEWPOINT_SPECIFICATION_MODEL_NAME = "Viewpoint Specification Model name:";

    private static final String WIZARD_VIEWPOINT_SPECIFICATION_PROJECT = "Viewpoint Specification Project";

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onSetUpAfterOpeningDesignerPerspective() throws Exception {
        designerPerspectives.openSiriusPerspective();
    }

    /**
     * Check that the VSM project is properly created.
     */
    public void test_ViewpointViewpoint_Specification_Project_Creation() {
        if (TestsUtil.shouldSkipUnreliableTests()) {
            return;
        }
        IProject project = createViewpointSpecificationProject(bot, VSM_PROJECT_NAME, VSM);

        bot.waitUntil(new DefaultCondition() {

            public boolean test() throws Exception {
                IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(VSM_PROJECT_NAME);
                return project != null && project.exists() && project.isOpen();
            }

            public String getFailureMessage() {
                return "A project named " + VSM_PROJECT_NAME + " should be created";
            }
        });
        SWTBotUtils.waitAllUiEvents();

        // Get project
        assertTrue("The created VSM project should exist.", project.exists());
        assertTrue("The created VSM project should be open.", project.isOpen());

        checkNatures(project);

        // Check the created folders
        assertNotNull(VSP_SHOULD_CONTAIN + "a folder named src.", project.getFolder("src"));
        assertNotNull(VSP_SHOULD_CONTAIN + "a folder named description.", project.getFolder("description"));
        assertNotNull(VSP_SHOULD_CONTAIN + "a folder named META-INF", project.getFolder("META-INF"));

        // Check the created files
        IFile vsm = project.getFile("description/" + VSM);
        assertNotNull(VSP_SHOULD_CONTAIN + "an odesign in the description folder. The odesign name should be " + VSM, vsm);
        assertNotNull(VSP_SHOULD_CONTAIN + "a plugin.xml file." + VSM, project.getFile("plugin.xml"));
        assertNotNull(VSP_SHOULD_CONTAIN + "a build.properties file." + VSM, project.getFile("build.properties"));
        assertNotNull(VSP_SHOULD_CONTAIN + ".classpath file.", project.getFile(".classpath"));
        assertNotNull(VSP_SHOULD_CONTAIN + ".project file.", project.getFile(".project"));
        assertNotNull(VSP_SHOULD_CONTAIN + "MANIFEST.MF file.", project.getFile("META-INF/MANIFEST.MF"));

        // Check that the created odesign does not need migration (version tag
        // must be initialized)
        VSMVersionSAXParser parser = new VSMVersionSAXParser(URI.createPlatformResourceURI(vsm.getFullPath().toOSString()));
        String loadedVersion = parser.getVersion(new NullProgressMonitor());
        assertNotNull("The VSM Group version must be initialized at creation.", loadedVersion);
        assertFalse("The migration must be required on just created VSM.", VSMMigrationService.getInstance().isMigrationNeeded(Version.parseVersion(loadedVersion)));

        // delete project
        closeAllEditors();
        try {
            project.delete(true, new NullProgressMonitor());
        } catch (CoreException e) {
            fail("Cannot delete the VSM Project");
        }

    }

    private void checkNatures(IProject project) {
        // Check the natures
        IProjectNature nature = null;
        try {
            nature = project.getNature("org.eclipse.acceleo.ide.ui.acceleoNature");
        } catch (CoreException e1) {
            fail("Cannot get the acceleo nature.");
        }
        assertNotNull("Acceleo should add its nature.", nature);

        nature = null;
        try {
            nature = project.getNature("org.eclipse.pde.PluginNature");
        } catch (CoreException e1) {
            fail("Cannot get the plugin nature.");
        }
        assertNotNull("The VSM PRoject should have a Plugin nature.", nature);

        nature = null;
        try {
            nature = project.getNature("org.eclipse.jdt.core.javanature");
        } catch (CoreException e1) {
            fail("Cannot get the java nature.");
        }
        assertNotNull("The VSM PRoject should be a Java Project.", nature);
    }

    /**
     * Create a Sirius Specification Project and wait until the creation is
     * done.
     * 
     * @param vsmProjectName
     * 
     * @return the created project.
     */
    public static IProject createViewpointSpecificationProject(SWTGefBot bot, final String vsmProjectName, String vsmFileName) {
        bot.menu(WIZARD_FILE).menu(WIZARD_NEW).menu(WIZARD_VIEWPOINT_SPECIFICATION_PROJECT).click();
        bot.waitUntilWidgetAppears(Conditions.shellIsActive(WIZARD_NEW + " " + WIZARD_VIEWPOINT_SPECIFICATION_PROJECT));

        bot.textWithLabel(WIZARD_PROJECT_NAME).setText(vsmProjectName);

        bot.waitUntil(new ItemEnabledCondition(bot.button(WIZARD_NEXT)));
        bot.button(WIZARD_NEXT).click();

        bot.textWithLabel(WIZARD_VIEWPOINT_SPECIFICATION_MODEL_NAME).setText("test.design");
        assertFalse("The wizard should not accept other file extension than odesign.", bot.button("Finish").isEnabled());

        bot.textWithLabel(WIZARD_VIEWPOINT_SPECIFICATION_MODEL_NAME).setText(vsmFileName);
        bot.waitUntil(new ItemEnabledCondition(bot.button(WIZARD_FINISH)));
        bot.button(WIZARD_FINISH).click();

        SWTBotUtils.waitAllUiEvents();

        bot.waitUntil(new DefaultCondition() {

            public boolean test() throws Exception {
                IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(VSM_PROJECT_NAME);
                return project != null && project.exists() && project.isOpen();
            }

            public String getFailureMessage() {
                return "A project named " + vsmProjectName + " should be created";
            }
        });

        return ResourcesPlugin.getWorkspace().getRoot().getProject(vsmProjectName);
    }

}