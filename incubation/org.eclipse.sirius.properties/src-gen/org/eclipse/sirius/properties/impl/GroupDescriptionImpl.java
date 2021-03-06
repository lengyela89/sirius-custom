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
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.sirius.properties.ContainerDescription;
import org.eclipse.sirius.properties.GroupDescription;
import org.eclipse.sirius.properties.GroupValidationSetDescription;
import org.eclipse.sirius.properties.PropertiesPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Group Description</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>
 * {@link org.eclipse.sirius.properties.impl.GroupDescriptionImpl#getIdentifier
 * <em>Identifier</em>}</li>
 * <li>
 * {@link org.eclipse.sirius.properties.impl.GroupDescriptionImpl#getLabelExpression
 * <em>Label Expression</em>}</li>
 * <li>
 * {@link org.eclipse.sirius.properties.impl.GroupDescriptionImpl#getDomainClass
 * <em>Domain Class</em>}</li>
 * <li>
 * {@link org.eclipse.sirius.properties.impl.GroupDescriptionImpl#getSemanticCandidateExpression
 * <em>Semantic Candidate Expression</em>}</li>
 * <li>
 * {@link org.eclipse.sirius.properties.impl.GroupDescriptionImpl#getContainer
 * <em>Container</em>}</li>
 * <li>
 * {@link org.eclipse.sirius.properties.impl.GroupDescriptionImpl#getValidationSet
 * <em>Validation Set</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GroupDescriptionImpl extends MinimalEObjectImpl.Container implements GroupDescription {
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
    protected String identifier = GroupDescriptionImpl.IDENTIFIER_EDEFAULT;

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
    protected String labelExpression = GroupDescriptionImpl.LABEL_EXPRESSION_EDEFAULT;

    /**
     * The default value of the '{@link #getDomainClass() <em>Domain Class</em>}
     * ' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getDomainClass()
     * @generated
     * @ordered
     */
    protected static final String DOMAIN_CLASS_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getDomainClass() <em>Domain Class</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getDomainClass()
     * @generated
     * @ordered
     */
    protected String domainClass = GroupDescriptionImpl.DOMAIN_CLASS_EDEFAULT;

    /**
     * The default value of the '{@link #getSemanticCandidateExpression()
     * <em>Semantic Candidate Expression</em>}' attribute. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @see #getSemanticCandidateExpression()
     * @generated
     * @ordered
     */
    protected static final String SEMANTIC_CANDIDATE_EXPRESSION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getSemanticCandidateExpression()
     * <em>Semantic Candidate Expression</em>}' attribute. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @see #getSemanticCandidateExpression()
     * @generated
     * @ordered
     */
    protected String semanticCandidateExpression = GroupDescriptionImpl.SEMANTIC_CANDIDATE_EXPRESSION_EDEFAULT;

    /**
     * The cached value of the '{@link #getContainer() <em>Container</em>}'
     * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getContainer()
     * @generated
     * @ordered
     */
    protected ContainerDescription container;

    /**
     * The cached value of the '{@link #getValidationSet()
     * <em>Validation Set</em>}' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getValidationSet()
     * @generated
     * @ordered
     */
    protected GroupValidationSetDescription validationSet;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected GroupDescriptionImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return PropertiesPackage.Literals.GROUP_DESCRIPTION;
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
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.GROUP_DESCRIPTION__IDENTIFIER, oldIdentifier, identifier));
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
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.GROUP_DESCRIPTION__LABEL_EXPRESSION, oldLabelExpression, labelExpression));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getDomainClass() {
        return domainClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setDomainClass(String newDomainClass) {
        String oldDomainClass = domainClass;
        domainClass = newDomainClass;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.GROUP_DESCRIPTION__DOMAIN_CLASS, oldDomainClass, domainClass));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getSemanticCandidateExpression() {
        return semanticCandidateExpression;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setSemanticCandidateExpression(String newSemanticCandidateExpression) {
        String oldSemanticCandidateExpression = semanticCandidateExpression;
        semanticCandidateExpression = newSemanticCandidateExpression;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.GROUP_DESCRIPTION__SEMANTIC_CANDIDATE_EXPRESSION, oldSemanticCandidateExpression, semanticCandidateExpression));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ContainerDescription getContainer() {
        return container;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetContainer(ContainerDescription newContainer, NotificationChain msgs) {
        ContainerDescription oldContainer = container;
        container = newContainer;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PropertiesPackage.GROUP_DESCRIPTION__CONTAINER, oldContainer, newContainer);
            if (msgs == null) {
                msgs = notification;
            } else {
                msgs.add(notification);
            }
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setContainer(ContainerDescription newContainer) {
        if (newContainer != container) {
            NotificationChain msgs = null;
            if (container != null) {
                msgs = ((InternalEObject) container).eInverseRemove(this, InternalEObject.EOPPOSITE_FEATURE_BASE - PropertiesPackage.GROUP_DESCRIPTION__CONTAINER, null, msgs);
            }
            if (newContainer != null) {
                msgs = ((InternalEObject) newContainer).eInverseAdd(this, InternalEObject.EOPPOSITE_FEATURE_BASE - PropertiesPackage.GROUP_DESCRIPTION__CONTAINER, null, msgs);
            }
            msgs = basicSetContainer(newContainer, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.GROUP_DESCRIPTION__CONTAINER, newContainer, newContainer));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public GroupValidationSetDescription getValidationSet() {
        return validationSet;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetValidationSet(GroupValidationSetDescription newValidationSet, NotificationChain msgs) {
        GroupValidationSetDescription oldValidationSet = validationSet;
        validationSet = newValidationSet;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PropertiesPackage.GROUP_DESCRIPTION__VALIDATION_SET, oldValidationSet, newValidationSet);
            if (msgs == null) {
                msgs = notification;
            } else {
                msgs.add(notification);
            }
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setValidationSet(GroupValidationSetDescription newValidationSet) {
        if (newValidationSet != validationSet) {
            NotificationChain msgs = null;
            if (validationSet != null) {
                msgs = ((InternalEObject) validationSet).eInverseRemove(this, InternalEObject.EOPPOSITE_FEATURE_BASE - PropertiesPackage.GROUP_DESCRIPTION__VALIDATION_SET, null, msgs);
            }
            if (newValidationSet != null) {
                msgs = ((InternalEObject) newValidationSet).eInverseAdd(this, InternalEObject.EOPPOSITE_FEATURE_BASE - PropertiesPackage.GROUP_DESCRIPTION__VALIDATION_SET, null, msgs);
            }
            msgs = basicSetValidationSet(newValidationSet, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, PropertiesPackage.GROUP_DESCRIPTION__VALIDATION_SET, newValidationSet, newValidationSet));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
        case PropertiesPackage.GROUP_DESCRIPTION__CONTAINER:
            return basicSetContainer(null, msgs);
        case PropertiesPackage.GROUP_DESCRIPTION__VALIDATION_SET:
            return basicSetValidationSet(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
        case PropertiesPackage.GROUP_DESCRIPTION__IDENTIFIER:
            return getIdentifier();
        case PropertiesPackage.GROUP_DESCRIPTION__LABEL_EXPRESSION:
            return getLabelExpression();
        case PropertiesPackage.GROUP_DESCRIPTION__DOMAIN_CLASS:
            return getDomainClass();
        case PropertiesPackage.GROUP_DESCRIPTION__SEMANTIC_CANDIDATE_EXPRESSION:
            return getSemanticCandidateExpression();
        case PropertiesPackage.GROUP_DESCRIPTION__CONTAINER:
            return getContainer();
        case PropertiesPackage.GROUP_DESCRIPTION__VALIDATION_SET:
            return getValidationSet();
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
        case PropertiesPackage.GROUP_DESCRIPTION__IDENTIFIER:
            setIdentifier((String) newValue);
            return;
        case PropertiesPackage.GROUP_DESCRIPTION__LABEL_EXPRESSION:
            setLabelExpression((String) newValue);
            return;
        case PropertiesPackage.GROUP_DESCRIPTION__DOMAIN_CLASS:
            setDomainClass((String) newValue);
            return;
        case PropertiesPackage.GROUP_DESCRIPTION__SEMANTIC_CANDIDATE_EXPRESSION:
            setSemanticCandidateExpression((String) newValue);
            return;
        case PropertiesPackage.GROUP_DESCRIPTION__CONTAINER:
            setContainer((ContainerDescription) newValue);
            return;
        case PropertiesPackage.GROUP_DESCRIPTION__VALIDATION_SET:
            setValidationSet((GroupValidationSetDescription) newValue);
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
        case PropertiesPackage.GROUP_DESCRIPTION__IDENTIFIER:
            setIdentifier(GroupDescriptionImpl.IDENTIFIER_EDEFAULT);
            return;
        case PropertiesPackage.GROUP_DESCRIPTION__LABEL_EXPRESSION:
            setLabelExpression(GroupDescriptionImpl.LABEL_EXPRESSION_EDEFAULT);
            return;
        case PropertiesPackage.GROUP_DESCRIPTION__DOMAIN_CLASS:
            setDomainClass(GroupDescriptionImpl.DOMAIN_CLASS_EDEFAULT);
            return;
        case PropertiesPackage.GROUP_DESCRIPTION__SEMANTIC_CANDIDATE_EXPRESSION:
            setSemanticCandidateExpression(GroupDescriptionImpl.SEMANTIC_CANDIDATE_EXPRESSION_EDEFAULT);
            return;
        case PropertiesPackage.GROUP_DESCRIPTION__CONTAINER:
            setContainer((ContainerDescription) null);
            return;
        case PropertiesPackage.GROUP_DESCRIPTION__VALIDATION_SET:
            setValidationSet((GroupValidationSetDescription) null);
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
        case PropertiesPackage.GROUP_DESCRIPTION__IDENTIFIER:
            return GroupDescriptionImpl.IDENTIFIER_EDEFAULT == null ? identifier != null : !GroupDescriptionImpl.IDENTIFIER_EDEFAULT.equals(identifier);
        case PropertiesPackage.GROUP_DESCRIPTION__LABEL_EXPRESSION:
            return GroupDescriptionImpl.LABEL_EXPRESSION_EDEFAULT == null ? labelExpression != null : !GroupDescriptionImpl.LABEL_EXPRESSION_EDEFAULT.equals(labelExpression);
        case PropertiesPackage.GROUP_DESCRIPTION__DOMAIN_CLASS:
            return GroupDescriptionImpl.DOMAIN_CLASS_EDEFAULT == null ? domainClass != null : !GroupDescriptionImpl.DOMAIN_CLASS_EDEFAULT.equals(domainClass);
        case PropertiesPackage.GROUP_DESCRIPTION__SEMANTIC_CANDIDATE_EXPRESSION:
            return GroupDescriptionImpl.SEMANTIC_CANDIDATE_EXPRESSION_EDEFAULT == null ? semanticCandidateExpression != null : !GroupDescriptionImpl.SEMANTIC_CANDIDATE_EXPRESSION_EDEFAULT
            .equals(semanticCandidateExpression);
        case PropertiesPackage.GROUP_DESCRIPTION__CONTAINER:
            return container != null;
        case PropertiesPackage.GROUP_DESCRIPTION__VALIDATION_SET:
            return validationSet != null;
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
        result.append(", domainClass: ");
        result.append(domainClass);
        result.append(", semanticCandidateExpression: ");
        result.append(semanticCandidateExpression);
        result.append(')');
        return result.toString();
    }

} // GroupDescriptionImpl
