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
package org.eclipse.sirius.properties.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.sirius.properties.ButtonDescription;
import org.eclipse.sirius.properties.CheckboxDescription;
import org.eclipse.sirius.properties.ContainerDescription;
import org.eclipse.sirius.properties.DynamicMappingFor;
import org.eclipse.sirius.properties.DynamicMappingIf;
import org.eclipse.sirius.properties.GroupDescription;
import org.eclipse.sirius.properties.GroupValidationSetDescription;
import org.eclipse.sirius.properties.LabelDescription;
import org.eclipse.sirius.properties.MultipleReferencesDescription;
import org.eclipse.sirius.properties.OperationDescription;
import org.eclipse.sirius.properties.PageDescription;
import org.eclipse.sirius.properties.PageValidationSetDescription;
import org.eclipse.sirius.properties.PropertiesFactory;
import org.eclipse.sirius.properties.PropertiesPackage;
import org.eclipse.sirius.properties.PropertyValidationRule;
import org.eclipse.sirius.properties.RadioDescription;
import org.eclipse.sirius.properties.SelectDescription;
import org.eclipse.sirius.properties.SingleReferenceDescription;
import org.eclipse.sirius.properties.TextAreaDescription;
import org.eclipse.sirius.properties.TextDescription;
import org.eclipse.sirius.properties.ViewExtensionDescription;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory</b>. <!--
 * end-user-doc -->
 *
 * @generated
 */
public class PropertiesFactoryImpl extends EFactoryImpl implements PropertiesFactory {
    /**
     * Creates the default factory implementation. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     */
    public static PropertiesFactory init() {
        try {
            PropertiesFactory thePropertiesFactory = (PropertiesFactory) EPackage.Registry.INSTANCE.getEFactory(PropertiesPackage.eNS_URI);
            if (thePropertiesFactory != null) {
                return thePropertiesFactory;
            }
        } catch (Exception exception) {
            EcorePlugin.INSTANCE.log(exception);
        }
        return new PropertiesFactoryImpl();
    }

    /**
     * Creates an instance of the factory. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     */
    public PropertiesFactoryImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EObject create(EClass eClass) {
        switch (eClass.getClassifierID()) {
        case PropertiesPackage.VIEW_EXTENSION_DESCRIPTION:
            return createViewExtensionDescription();
        case PropertiesPackage.PAGE_DESCRIPTION:
            return createPageDescription();
        case PropertiesPackage.PAGE_VALIDATION_SET_DESCRIPTION:
            return createPageValidationSetDescription();
        case PropertiesPackage.PROPERTY_VALIDATION_RULE:
            return createPropertyValidationRule();
        case PropertiesPackage.GROUP_DESCRIPTION:
            return createGroupDescription();
        case PropertiesPackage.GROUP_VALIDATION_SET_DESCRIPTION:
            return createGroupValidationSetDescription();
        case PropertiesPackage.CONTAINER_DESCRIPTION:
            return createContainerDescription();
        case PropertiesPackage.TEXT_DESCRIPTION:
            return createTextDescription();
        case PropertiesPackage.BUTTON_DESCRIPTION:
            return createButtonDescription();
        case PropertiesPackage.LABEL_DESCRIPTION:
            return createLabelDescription();
        case PropertiesPackage.CHECKBOX_DESCRIPTION:
            return createCheckboxDescription();
        case PropertiesPackage.SELECT_DESCRIPTION:
            return createSelectDescription();
        case PropertiesPackage.DYNAMIC_MAPPING_FOR:
            return createDynamicMappingFor();
        case PropertiesPackage.DYNAMIC_MAPPING_IF:
            return createDynamicMappingIf();
        case PropertiesPackage.TEXT_AREA_DESCRIPTION:
            return createTextAreaDescription();
        case PropertiesPackage.RADIO_DESCRIPTION:
            return createRadioDescription();
        case PropertiesPackage.SINGLE_REFERENCE_DESCRIPTION:
            return createSingleReferenceDescription();
        case PropertiesPackage.OPERATION_DESCRIPTION:
            return createOperationDescription();
        case PropertiesPackage.MULTIPLE_REFERENCES_DESCRIPTION:
            return createMultipleReferencesDescription();
        default:
            throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ViewExtensionDescription createViewExtensionDescription() {
        ViewExtensionDescriptionImpl viewExtensionDescription = new ViewExtensionDescriptionImpl();
        return viewExtensionDescription;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public PageDescription createPageDescription() {
        PageDescriptionImpl pageDescription = new PageDescriptionImpl();
        return pageDescription;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public PageValidationSetDescription createPageValidationSetDescription() {
        PageValidationSetDescriptionImpl pageValidationSetDescription = new PageValidationSetDescriptionImpl();
        return pageValidationSetDescription;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public PropertyValidationRule createPropertyValidationRule() {
        PropertyValidationRuleImpl propertyValidationRule = new PropertyValidationRuleImpl();
        return propertyValidationRule;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public GroupDescription createGroupDescription() {
        GroupDescriptionImpl groupDescription = new GroupDescriptionImpl();
        return groupDescription;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public GroupValidationSetDescription createGroupValidationSetDescription() {
        GroupValidationSetDescriptionImpl groupValidationSetDescription = new GroupValidationSetDescriptionImpl();
        return groupValidationSetDescription;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ContainerDescription createContainerDescription() {
        ContainerDescriptionImpl containerDescription = new ContainerDescriptionImpl();
        return containerDescription;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public TextDescription createTextDescription() {
        TextDescriptionImpl textDescription = new TextDescriptionImpl();
        return textDescription;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ButtonDescription createButtonDescription() {
        ButtonDescriptionImpl buttonDescription = new ButtonDescriptionImpl();
        return buttonDescription;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public LabelDescription createLabelDescription() {
        LabelDescriptionImpl labelDescription = new LabelDescriptionImpl();
        return labelDescription;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public CheckboxDescription createCheckboxDescription() {
        CheckboxDescriptionImpl checkboxDescription = new CheckboxDescriptionImpl();
        return checkboxDescription;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public SelectDescription createSelectDescription() {
        SelectDescriptionImpl selectDescription = new SelectDescriptionImpl();
        return selectDescription;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public DynamicMappingFor createDynamicMappingFor() {
        DynamicMappingForImpl dynamicMappingFor = new DynamicMappingForImpl();
        return dynamicMappingFor;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public DynamicMappingIf createDynamicMappingIf() {
        DynamicMappingIfImpl dynamicMappingIf = new DynamicMappingIfImpl();
        return dynamicMappingIf;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public TextAreaDescription createTextAreaDescription() {
        TextAreaDescriptionImpl textAreaDescription = new TextAreaDescriptionImpl();
        return textAreaDescription;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public RadioDescription createRadioDescription() {
        RadioDescriptionImpl radioDescription = new RadioDescriptionImpl();
        return radioDescription;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public SingleReferenceDescription createSingleReferenceDescription() {
        SingleReferenceDescriptionImpl singleReferenceDescription = new SingleReferenceDescriptionImpl();
        return singleReferenceDescription;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public OperationDescription createOperationDescription() {
        OperationDescriptionImpl operationDescription = new OperationDescriptionImpl();
        return operationDescription;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public MultipleReferencesDescription createMultipleReferencesDescription() {
        MultipleReferencesDescriptionImpl multipleReferencesDescription = new MultipleReferencesDescriptionImpl();
        return multipleReferencesDescription;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public PropertiesPackage getPropertiesPackage() {
        return (PropertiesPackage) getEPackage();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @deprecated
     * @generated
     */
    @Deprecated
    public static PropertiesPackage getPackage() {
        return PropertiesPackage.eINSTANCE;
    }

} // PropertiesFactoryImpl
