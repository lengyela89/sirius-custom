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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.sirius.properties.PropertiesPackage;
import org.eclipse.sirius.properties.WidgetDescription;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Widget Description</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>
 * {@link org.eclipse.sirius.properties.impl.WidgetDescriptionImpl#getIdentifier
 * <em>Identifier</em>}</li>
 * <li>
 * {@link org.eclipse.sirius.properties.impl.WidgetDescriptionImpl#getLabelExpression
 * <em>Label Expression</em>}</li>
 * <li>
 * {@link org.eclipse.sirius.properties.impl.WidgetDescriptionImpl#getHelpExpression
 * <em>Help Expression</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class WidgetDescriptionImpl extends MinimalEObjectImpl.Container implements WidgetDescription {
    /**
     * The default value of the '{@link #getIdentifier() <em>Identifier</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getIdentifier()
     * @generated
     * @ordered
     */
    protected static final String IDENTIFIER_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getIdentifier() <em>Identifier</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getIdentifier()
     * @generated
     * @ordered
     */
    protected String identifier = WidgetDescriptionImpl.IDENTIFIER_EDEFAULT;

    /**
     * The default value of the '{@link #getLabelExpression()
     * <em>Label Expression</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getLabelExpression()
     * @generated
     * @ordered
     */
    protected static final String LABEL_EXPRESSION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getLabelExpression()
     * <em>Label Expression</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getLabelExpression()
     * @generated
     * @ordered
     */
    protected String labelExpression = WidgetDescriptionImpl.LABEL_EXPRESSION_EDEFAULT;

    /**
     * The default value of the '{@link #getHelpExpression()
     * <em>Help Expression</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getHelpExpression()
     * @generated
     * @ordered
     */
    protected static final String HELP_EXPRESSION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getHelpExpression()
     * <em>Help Expression</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getHelpExpression()
     * @generated
     * @ordered
     */
    protected String helpExpression = WidgetDescriptionImpl.HELP_EXPRESSION_EDEFAULT;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected WidgetDescriptionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return PropertiesPackage.Literals.WIDGET_DESCRIPTION;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getIdentifier() {
        return identifier;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setIdentifier(String newIdentifier) {
        String oldIdentifier = identifier;
        identifier = newIdentifier;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.WIDGET_DESCRIPTION__IDENTIFIER, oldIdentifier, identifier));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getLabelExpression() {
        return labelExpression;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setLabelExpression(String newLabelExpression) {
        String oldLabelExpression = labelExpression;
        labelExpression = newLabelExpression;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.WIDGET_DESCRIPTION__LABEL_EXPRESSION, oldLabelExpression, labelExpression));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getHelpExpression() {
        return helpExpression;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setHelpExpression(String newHelpExpression) {
        String oldHelpExpression = helpExpression;
        helpExpression = newHelpExpression;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.WIDGET_DESCRIPTION__HELP_EXPRESSION, oldHelpExpression, helpExpression));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case PropertiesPackage.WIDGET_DESCRIPTION__IDENTIFIER:
            return getIdentifier();
        case PropertiesPackage.WIDGET_DESCRIPTION__LABEL_EXPRESSION:
            return getLabelExpression();
        case PropertiesPackage.WIDGET_DESCRIPTION__HELP_EXPRESSION:
            return getHelpExpression();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
        case PropertiesPackage.WIDGET_DESCRIPTION__IDENTIFIER:
            setIdentifier((String) newValue);
            return;
        case PropertiesPackage.WIDGET_DESCRIPTION__LABEL_EXPRESSION:
            setLabelExpression((String) newValue);
            return;
        case PropertiesPackage.WIDGET_DESCRIPTION__HELP_EXPRESSION:
            setHelpExpression((String) newValue);
            return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
        case PropertiesPackage.WIDGET_DESCRIPTION__IDENTIFIER:
            setIdentifier(WidgetDescriptionImpl.IDENTIFIER_EDEFAULT);
            return;
        case PropertiesPackage.WIDGET_DESCRIPTION__LABEL_EXPRESSION:
            setLabelExpression(WidgetDescriptionImpl.LABEL_EXPRESSION_EDEFAULT);
            return;
        case PropertiesPackage.WIDGET_DESCRIPTION__HELP_EXPRESSION:
            setHelpExpression(WidgetDescriptionImpl.HELP_EXPRESSION_EDEFAULT);
            return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
        case PropertiesPackage.WIDGET_DESCRIPTION__IDENTIFIER:
            return WidgetDescriptionImpl.IDENTIFIER_EDEFAULT == null ? identifier != null : !WidgetDescriptionImpl.IDENTIFIER_EDEFAULT.equals(identifier);
        case PropertiesPackage.WIDGET_DESCRIPTION__LABEL_EXPRESSION:
            return WidgetDescriptionImpl.LABEL_EXPRESSION_EDEFAULT == null ? labelExpression != null : !WidgetDescriptionImpl.LABEL_EXPRESSION_EDEFAULT.equals(labelExpression);
        case PropertiesPackage.WIDGET_DESCRIPTION__HELP_EXPRESSION:
            return WidgetDescriptionImpl.HELP_EXPRESSION_EDEFAULT == null ? helpExpression != null : !WidgetDescriptionImpl.HELP_EXPRESSION_EDEFAULT.equals(helpExpression);
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) {
            return super.toString();
        }

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (identifier: ");
        result.append(identifier);
        result.append(", labelExpression: ");
        result.append(labelExpression);
        result.append(", helpExpression: ");
        result.append(helpExpression);
        result.append(')');
        return result.toString();
    }

} // WidgetDescriptionImpl
