/**
 * Copyright (c) 2016 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *
 */
package org.eclipse.sirius.properties;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc --> The <b>Factory</b> for the model. It provides a
 * create method for each non-abstract class of the model. <!-- end-user-doc -->
 *
 * @see org.eclipse.sirius.properties.PropertiesPackage
 * @generated
 */
public interface PropertiesFactory extends EFactory {
    /**
     * The singleton instance of the factory. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     */
    PropertiesFactory eINSTANCE = org.eclipse.sirius.properties.impl.PropertiesFactoryImpl.init();

    /**
     * Returns a new object of class '<em>View Extension Description</em>'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return a new object of class '<em>View Extension Description</em>'.
     * @generated
     */
    ViewExtensionDescription createViewExtensionDescription();

    /**
     * Returns a new object of class '<em>Page Description</em>'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return a new object of class '<em>Page Description</em>'.
     * @generated
     */
    PageDescription createPageDescription();

    /**
     * Returns a new object of class '<em>Page Validation Set Description</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return a new object of class '<em>Page Validation Set Description</em>'.
     * @generated
     */
    PageValidationSetDescription createPageValidationSetDescription();

    /**
     * Returns a new object of class '<em>Property Validation Rule</em>'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return a new object of class '<em>Property Validation Rule</em>'.
     * @generated
     */
    PropertyValidationRule createPropertyValidationRule();

    /**
     * Returns a new object of class '<em>Group Description</em>'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return a new object of class '<em>Group Description</em>'.
     * @generated
     */
    GroupDescription createGroupDescription();

    /**
     * Returns a new object of class '<em>Group Validation Set Description</em>
     * '. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return a new object of class '<em>Group Validation Set Description</em>
     *         '.
     * @generated
     */
    GroupValidationSetDescription createGroupValidationSetDescription();

    /**
     * Returns a new object of class '<em>Container Description</em>'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return a new object of class '<em>Container Description</em>'.
     * @generated
     */
    ContainerDescription createContainerDescription();

    /**
     * Returns a new object of class '<em>Text Description</em>'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return a new object of class '<em>Text Description</em>'.
     * @generated
     */
    TextDescription createTextDescription();

    /**
     * Returns a new object of class '<em>Button Description</em>'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return a new object of class '<em>Button Description</em>'.
     * @generated
     */
    ButtonDescription createButtonDescription();

    /**
     * Returns a new object of class '<em>Label Description</em>'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return a new object of class '<em>Label Description</em>'.
     * @generated
     */
    LabelDescription createLabelDescription();

    /**
     * Returns a new object of class '<em>Checkbox Description</em>'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return a new object of class '<em>Checkbox Description</em>'.
     * @generated
     */
    CheckboxDescription createCheckboxDescription();

    /**
     * Returns a new object of class '<em>Select Description</em>'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return a new object of class '<em>Select Description</em>'.
     * @generated
     */
    SelectDescription createSelectDescription();

    /**
     * Returns a new object of class '<em>Dynamic Mapping For</em>'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return a new object of class '<em>Dynamic Mapping For</em>'.
     * @generated
     */
    DynamicMappingFor createDynamicMappingFor();

    /**
     * Returns a new object of class '<em>Dynamic Mapping If</em>'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return a new object of class '<em>Dynamic Mapping If</em>'.
     * @generated
     */
    DynamicMappingIf createDynamicMappingIf();

    /**
     * Returns a new object of class '<em>Text Area Description</em>'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return a new object of class '<em>Text Area Description</em>'.
     * @generated
     */
    TextAreaDescription createTextAreaDescription();

    /**
     * Returns a new object of class '<em>Radio Description</em>'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return a new object of class '<em>Radio Description</em>'.
     * @generated
     */
    RadioDescription createRadioDescription();

    /**
     * Returns a new object of class '<em>Single Reference Description</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return a new object of class '<em>Single Reference Description</em>'.
     * @generated
     */
    SingleReferenceDescription createSingleReferenceDescription();

    /**
     * Returns a new object of class '<em>Operation Description</em>'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return a new object of class '<em>Operation Description</em>'.
     * @generated
     */
    OperationDescription createOperationDescription();

    /**
     * Returns a new object of class '<em>Multiple References Description</em>'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return a new object of class '<em>Multiple References Description</em>'.
     * @generated
     */
    MultipleReferencesDescription createMultipleReferencesDescription();

    /**
     * Returns the package supported by this factory. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the package supported by this factory.
     * @generated
     */
    PropertiesPackage getPropertiesPackage();

} // PropertiesFactory
