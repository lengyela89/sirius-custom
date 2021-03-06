/*******************************************************************************
 * Copyright (c) 2010, 2015 THALES GLOBAL SERVICES, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.tests.unit.diagram.vsm;

import java.util.List;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.sirius.ecore.extender.tool.api.ModelUtils;
import org.eclipse.sirius.tests.SiriusTestsPlugin;
import org.eclipse.sirius.tests.support.api.EclipseTestsSupportHelper;
import org.eclipse.sirius.tests.support.api.SiriusDiagramTestCase;
import org.eclipse.sirius.viewpoint.description.Group;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;

/**
 * Test VSM validation. VP-2506, VP-2475, VP-3836
 * 
 * @author <a href="mailto:julien.dupont@obeo.fr">Julien DUPONT</a>
 */
public class VSMValidationTest extends SiriusDiagramTestCase {

    private Group modeler;

    private Group modelerWithNoStyle;

    private Group modelerWithDiagramExtension;

    private Group modelerForDomainClassValidation;

    private Group modelerForVariableNameValidation;

    private Group modelerForImagePathValidation;

    private Group modelerForDefaultLayerValidation;

    public void setUp() throws Exception {
        ResourceSet set = new ResourceSetImpl();
        EclipseTestsSupportHelper.INSTANCE.createProject("Project");
        EclipseTestsSupportHelper.INSTANCE.copyFile(SiriusTestsPlugin.PLUGIN_ID, "/images/es.png", "/Project/es.png");
        modeler = (Group) ModelUtils.load(URI.createPlatformPluginURI("/org.eclipse.sirius.tests.junit/data/unit/vsm/valideVSM.odesign", true), set);
        modelerWithNoStyle = (Group) ModelUtils.load(URI.createPlatformPluginURI("/org.eclipse.sirius.tests.junit/data/unit/vsm/validateVSMWithNoStyle.odesign", true), set);
        modelerWithDiagramExtension = (Group) ModelUtils.load(URI.createPlatformPluginURI("/org.eclipse.sirius.tests.junit/data/unit/vsm/valideVSMWithDiagramExtension.odesign", true), set);
        modelerForDomainClassValidation = (Group) ModelUtils.load(URI.createPlatformPluginURI("/org.eclipse.sirius.tests.junit/data/unit/vsm/valideDomainClassVSM.odesign", true), set);
        modelerForVariableNameValidation = (Group) ModelUtils.load(URI.createPlatformPluginURI("/org.eclipse.sirius.tests.junit/data/unit/vsm/valideVariableNameVSM.odesign", true), set);
        modelerForImagePathValidation = (Group) ModelUtils.load(URI.createPlatformPluginURI("/org.eclipse.sirius.tests.junit/data/unit/vsm/validateImagePathVSM.odesign", true), set);
        modelerForDefaultLayerValidation = (Group) ModelUtils.load(URI.createPlatformPluginURI("/org.eclipse.sirius.tests.junit/data/unit/vsm/validateDefaultLayerVSM.odesign", true), set);
    }

    /**
     * Test VSM validation. Test there in no error when validate VSM.
     */
    public void testValidationVSM() {
        // Test that the modeler is valid. In this case the modeler is valid, so
        // test if diagnostic is ok.
        Diagnostician diagnostician = new Diagnostician();
        Diagnostic diagnostic = diagnostician.validate(modeler);
        assertEquals("The VSM is valid, it should not have popup error message", Diagnostic.OK, diagnostic.getSeverity());
    }

    /**
     * Test VSM validation with diagram extension (see VP-3836). Test there in
     * no error when validate VSM.
     */
    public void testValidationVSMWithDiagramExtension() {
        // Test that the modeler is valid. In this case the modeler is valid, so
        // test if diagnostic is ok.
        Diagnostician diagnostician = new Diagnostician();
        Diagnostic diagnostic = diagnostician.validate(modelerWithDiagramExtension);
        assertEquals("The VSM is valid, it should not have popup error message", Diagnostic.OK, diagnostic.getSeverity());
    }

    /**
     * Test VSM validation with element mapping with no style. Element mapping
     * must be have a style. Test VP-2475
     */
    public void testValidationVSMWithElementMappingNoStyle() {
        Diagnostician diagnostician = new Diagnostician();
        Diagnostic diagnostic = diagnostician.validate(modelerWithNoStyle);
        // Check that there is a pop up for validation problems
        assertEquals("The VSM is not valid, it should have popup error message", Diagnostic.ERROR, diagnostic.getSeverity());
        // The VSM for test have a node mapping without style, with conditional
        // test without style, a containerMapping without style, with a
        // conditional style without style, a relation based edge without style
        // with conditional style without style, a element based edge without
        // style with conditional style without style and node,container and
        // edge import without style (this 3 items with no style are valid)
        assertEquals("The diagnostic must contain 6 elements invalidated", 6, diagnostic.getChildren().size());
        for (Diagnostic diag : diagnostic.getChildren()) {
            assertEquals("The list of invalidated elements must start by 'A style is missing for' and nothing else", "A style is missing for", diag.getMessage().substring(0, 22));
        }
    }

    /**
     * Test VSM validation when the domain class name contains white spaces
     * after, before or both.
     */
    public void testValidationWithWhiteSpaceAfterAndBeforeDomainClassName() {
        // Ensure that the domain class name contains white space (before, after
        // or both).
        TreeIterator<EObject> elements = modelerForDomainClassValidation.eAllContents();
        // Variable to make choice between add space before (0), after
        // (1) or both (2) domain class name
        int i = 0;
        while (elements.hasNext()) {
            EObject current = elements.next();
            for (EAttribute attribute : current.eClass().getEAllAttributes()) {
                if ("domainClass".equals(attribute.getName())) {
                    addSpaceInDomainClassValue(current, attribute, i);
                    if (i < 2) {
                        i++;
                    } else {
                        i = 0;
                    }
                }
            }
        }
        // Test that the modeler is valid. In this case the modeler is valid, so
        // test if diagnostic is ok.
        Diagnostician diagnostician = new Diagnostician();
        Diagnostic diagnostic = diagnostician.validate(modelerForDomainClassValidation);
        assertEquals("The VSM is valid, it should not have popup error message", Diagnostic.OK, diagnostic.getSeverity());
    }

    /**
     * Ensure that VSM validation check the Domain class name and fails when the
     * domain class name is invalid.
     */
    public void testValidationDomainClass() {
        // Ensure that the domain class name is invalid.
        TreeIterator<EObject> elements = modelerForDomainClassValidation.eAllContents();
        // variable to make choice between add chars before (0), after
        // (1), both (2) or in the middle (3) of domain class name
        int i = 0;
        while (elements.hasNext()) {
            EObject current = elements.next();
            for (EAttribute attribute : current.eClass().getEAllAttributes()) {
                if ("domainClass".equals(attribute.getName())) {
                    editDomainClassValue(current, attribute, i);
                    if (i < 3) {
                        i++;
                    } else {
                        i = 0;
                    }
                }
            }
        }
        // Test that the modeler is not valid. In this case the modeler is not
        // valid, so Check that there is a pop up for validation problems.
        Diagnostician diagnostician = new Diagnostician();
        Diagnostic diagnostic = diagnostician.validate(modelerForDomainClassValidation);
        assertEquals("The VSM is not valid, it should have popup error message", Diagnostic.ERROR, diagnostic.getSeverity());
    }

    /**
     * Ensure that VSM validation do not detects errors on the use of newly
     * defined variables with valid expression.
     */
    public void testVariableNameValidation() {
        // Test that the modeler is valid. In this case the modeler is valid, so
        // test if diagnostic is ok.
        Diagnostician diagnostician = new Diagnostician();
        Diagnostic diagnostic = diagnostician.validate(modelerForVariableNameValidation);
        assertEquals("The VSM is valid, it should not have popup error message", Diagnostic.OK, diagnostic.getSeverity());
    }

    /**
     * Test VSM validation with diferent paths for images (there are valid and
     * invalid paths).
     */
    public void testValidationImagePathVSM() {
        Diagnostician diagnostician = new Diagnostician();
        Diagnostic diagnostic = diagnostician.validate(modelerForImagePathValidation);
        // Check that there is a pop up for validation problems
        assertEquals("The VSM is not valid, it should have popup error message", Diagnostic.ERROR, diagnostic.getSeverity());
        List<Diagnostic> children = diagnostic.getChildren();
        String[] expectedMessagesPatterns = { "^The path '  /org.eclipse.sirius.tests.junit/images/logo_o.png   ' does not correspond to an image.$",
                "^The image '  /org.eclipse.sirius.tests.junit/images/logo_o.png   ' does not exist.$", "^The image '/test/noimage.gif' does not exist.$",
                "^The path 'icon' does not correspond to an image.$", "^The image 'icon' does not exist.$", "^The path '/org.eclipse.sirius.tests.junit/plugin.xml' does not correspond to an image.$",
                "^The image 'C:\\\\images\\\\image.png' does not exist.$", "^The image '/org.eclipse.sirius.tests.junit/images/notexisting.png' does not exist.$",
                "^The required feature 'decoratorPath' of 'org.eclipse.sirius.viewpoint.description.impl.SemanticBasedDecorationImpl@.*' must be set$"};
        
        assertEquals(
                "The diagnostic must contain " + expectedMessagesPatterns.length + " validation errors. Returned messages were :\n"
                        + Joiner.on('\n').join(Iterables.transform(children, new Function<Diagnostic, String>() {

                            @Override
                            public String apply(Diagnostic input) {
                                return input.getMessage();
                            }
                        })), expectedMessagesPatterns.length, children.size());
        for (int i = 0; i < expectedMessagesPatterns.length; i++) {
            assertTrue("Unexpected validation error at position " + i, children.get(i).getMessage().matches(expectedMessagesPatterns[i]));
        }
    }

    /**
     * Ensure that VSM validation detect errors for missing default layers.
     */
    public void testValidationDefaultLayer() {
        Diagnostician diagnostician = new Diagnostician();
        Diagnostic diagnostic = diagnostician.validate(modelerForDefaultLayerValidation);
        // Check that there is a pop up for validation problems
        assertEquals("The VSM is not valid, it should have popup error message", Diagnostic.ERROR, diagnostic.getSeverity());
        List<Diagnostic> children = diagnostic.getChildren();
        assertEquals("The diagnostic must contain 2 validation errors", 2, children.size());
        assertEquals("The first error does not match", "The default layer is missing for the diagram 'D1'.", children.get(0).getMessage());
        assertEquals("The second error does not match", "The default layer is missing for the diagram 'S2'.", children.get(1).getMessage());
    }

    private void addSpaceInDomainClassValue(EObject current, EAttribute attribute, int iterate) {

        switch (iterate) {
        case 0:
            current.eSet(attribute, current.eGet(attribute) + " ");
            break;
        case 1:
            current.eSet(attribute, " " + current.eGet(attribute));
            break;
        case 2:
            current.eSet(attribute, " " + current.eGet(attribute) + " ");
            break;
        default:
            break;
        }
    }

    private void editDomainClassValue(EObject current, EAttribute attribute, int iterate) {

        switch (iterate) {
        case 0:
            current.eSet(attribute, "AAA");
            break;
        case 1:
            current.eSet(attribute, "- " + current.eGet(attribute));
            break;
        case 2:
            current.eSet(attribute, "1E" + current.eGet(attribute) + "./");
            break;
        case 3:
            String value = current.eGet(attribute) + "";
            value.replace(value.charAt(1), ' ');
            current.eSet(attribute, value);
            break;
        default:
            break;
        }
    }

    public void tearDown() {
        modeler = null;
    }
}
