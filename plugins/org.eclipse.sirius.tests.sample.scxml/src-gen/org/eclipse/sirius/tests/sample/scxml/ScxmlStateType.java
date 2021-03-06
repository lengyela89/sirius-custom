/**
 * Copyright (c) 2015 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 */
package org.eclipse.sirius.tests.sample.scxml;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>State Type</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>
 * {@link org.eclipse.sirius.tests.sample.scxml.ScxmlStateType#getScxmlStateMix
 * <em>Scxml State Mix</em>}</li>
 * <li>{@link org.eclipse.sirius.tests.sample.scxml.ScxmlStateType#getOnentry
 * <em>Onentry</em>}</li>
 * <li>{@link org.eclipse.sirius.tests.sample.scxml.ScxmlStateType#getOnexit
 * <em>Onexit</em>}</li>
 * <li>
 * {@link org.eclipse.sirius.tests.sample.scxml.ScxmlStateType#getTransition
 * <em>Transition</em>}</li>
 * <li>{@link org.eclipse.sirius.tests.sample.scxml.ScxmlStateType#getInitial
 * <em>Initial</em>}</li>
 * <li>{@link org.eclipse.sirius.tests.sample.scxml.ScxmlStateType#getState <em>
 * State</em>}</li>
 * <li>{@link org.eclipse.sirius.tests.sample.scxml.ScxmlStateType#getParallel
 * <em>Parallel</em>}</li>
 * <li>{@link org.eclipse.sirius.tests.sample.scxml.ScxmlStateType#getFinal <em>
 * Final</em>}</li>
 * <li>{@link org.eclipse.sirius.tests.sample.scxml.ScxmlStateType#getHistory
 * <em>History</em>}</li>
 * <li>{@link org.eclipse.sirius.tests.sample.scxml.ScxmlStateType#getDatamodel
 * <em>Datamodel</em>}</li>
 * <li>{@link org.eclipse.sirius.tests.sample.scxml.ScxmlStateType#getInvoke
 * <em>Invoke</em>}</li>
 * <li>{@link org.eclipse.sirius.tests.sample.scxml.ScxmlStateType#getAny <em>
 * Any</em>}</li>
 * <li>{@link org.eclipse.sirius.tests.sample.scxml.ScxmlStateType#getId <em>Id
 * </em>}</li>
 * <li>{@link org.eclipse.sirius.tests.sample.scxml.ScxmlStateType#getInitial1
 * <em>Initial1</em>}</li>
 * <li>
 * {@link org.eclipse.sirius.tests.sample.scxml.ScxmlStateType#getAnyAttribute
 * <em>Any Attribute</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.sirius.tests.sample.scxml.ScxmlPackage#getScxmlStateType()
 * @model extendedMetaData="name='scxml.state.type' kind='elementOnly'"
 * @generated
 */
public interface ScxmlStateType extends EObject {
    /**
     * Returns the value of the '<em><b>Scxml State Mix</b></em>' attribute
     * list. The list contents are of type
     * {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}. <!-- begin-user-doc
     * -->
     * <p>
     * If the meaning of the '<em>Scxml State Mix</em>' attribute list isn't
     * clear, there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Scxml State Mix</em>' attribute list.
     * @see org.eclipse.sirius.tests.sample.scxml.ScxmlPackage#getScxmlStateType_ScxmlStateMix()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry"
     *        many="true" extendedMetaData="kind='group' name='ScxmlStateMix:0'"
     * @generated
     */
    FeatureMap getScxmlStateMix();

    /**
     * Returns the value of the '<em><b>Onentry</b></em>' containment reference
     * list. The list contents are of type
     * {@link org.eclipse.sirius.tests.sample.scxml.ScxmlOnentryType}. <!--
     * begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Onentry</em>' containment reference list isn't
     * clear, there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Onentry</em>' containment reference list.
     * @see org.eclipse.sirius.tests.sample.scxml.ScxmlPackage#getScxmlStateType_Onentry()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData=
     *        "kind='element' name='onentry' namespace='##targetNamespace' group='#ScxmlStateMix:0'"
     * @generated
     */
    EList<ScxmlOnentryType> getOnentry();

    /**
     * Returns the value of the '<em><b>Onexit</b></em>' containment reference
     * list. The list contents are of type
     * {@link org.eclipse.sirius.tests.sample.scxml.ScxmlOnexitType}. <!--
     * begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Onexit</em>' containment reference list isn't
     * clear, there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Onexit</em>' containment reference list.
     * @see org.eclipse.sirius.tests.sample.scxml.ScxmlPackage#getScxmlStateType_Onexit()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData=
     *        "kind='element' name='onexit' namespace='##targetNamespace' group='#ScxmlStateMix:0'"
     * @generated
     */
    EList<ScxmlOnexitType> getOnexit();

    /**
     * Returns the value of the '<em><b>Transition</b></em>' containment
     * reference list. The list contents are of type
     * {@link org.eclipse.sirius.tests.sample.scxml.ScxmlTransitionType}. <!--
     * begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Transition</em>' containment reference list
     * isn't clear, there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Transition</em>' containment reference
     *         list.
     * @see org.eclipse.sirius.tests.sample.scxml.ScxmlPackage#getScxmlStateType_Transition()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData=
     *        "kind='element' name='transition' namespace='##targetNamespace' group='#ScxmlStateMix:0'"
     * @generated
     */
    EList<ScxmlTransitionType> getTransition();

    /**
     * Returns the value of the '<em><b>Initial</b></em>' containment reference
     * list. The list contents are of type
     * {@link org.eclipse.sirius.tests.sample.scxml.ScxmlInitialType}. <!--
     * begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Initial</em>' containment reference list isn't
     * clear, there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Initial</em>' containment reference list.
     * @see org.eclipse.sirius.tests.sample.scxml.ScxmlPackage#getScxmlStateType_Initial()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData=
     *        "kind='element' name='initial' namespace='##targetNamespace' group='#ScxmlStateMix:0'"
     * @generated
     */
    EList<ScxmlInitialType> getInitial();

    /**
     * Returns the value of the '<em><b>State</b></em>' containment reference
     * list. The list contents are of type
     * {@link org.eclipse.sirius.tests.sample.scxml.ScxmlStateType}. <!--
     * begin-user-doc -->
     * <p>
     * If the meaning of the '<em>State</em>' containment reference list isn't
     * clear, there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>State</em>' containment reference list.
     * @see org.eclipse.sirius.tests.sample.scxml.ScxmlPackage#getScxmlStateType_State()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData=
     *        "kind='element' name='state' namespace='##targetNamespace' group='#ScxmlStateMix:0'"
     * @generated
     */
    EList<ScxmlStateType> getState();

    /**
     * Returns the value of the '<em><b>Parallel</b></em>' containment reference
     * list. The list contents are of type
     * {@link org.eclipse.sirius.tests.sample.scxml.ScxmlParallelType}. <!--
     * begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Parallel</em>' containment reference list
     * isn't clear, there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Parallel</em>' containment reference list.
     * @see org.eclipse.sirius.tests.sample.scxml.ScxmlPackage#getScxmlStateType_Parallel()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData=
     *        "kind='element' name='parallel' namespace='##targetNamespace' group='#ScxmlStateMix:0'"
     * @generated
     */
    EList<ScxmlParallelType> getParallel();

    /**
     * Returns the value of the '<em><b>Final</b></em>' containment reference
     * list. The list contents are of type
     * {@link org.eclipse.sirius.tests.sample.scxml.ScxmlFinalType}. <!--
     * begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Final</em>' containment reference list isn't
     * clear, there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Final</em>' containment reference list.
     * @see org.eclipse.sirius.tests.sample.scxml.ScxmlPackage#getScxmlStateType_Final()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData=
     *        "kind='element' name='final' namespace='##targetNamespace' group='#ScxmlStateMix:0'"
     * @generated
     */
    EList<ScxmlFinalType> getFinal();

    /**
     * Returns the value of the '<em><b>History</b></em>' containment reference
     * list. The list contents are of type
     * {@link org.eclipse.sirius.tests.sample.scxml.ScxmlHistoryType}. <!--
     * begin-user-doc -->
     * <p>
     * If the meaning of the '<em>History</em>' containment reference list isn't
     * clear, there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>History</em>' containment reference list.
     * @see org.eclipse.sirius.tests.sample.scxml.ScxmlPackage#getScxmlStateType_History()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData=
     *        "kind='element' name='history' namespace='##targetNamespace' group='#ScxmlStateMix:0'"
     * @generated
     */
    EList<ScxmlHistoryType> getHistory();

    /**
     * Returns the value of the '<em><b>Datamodel</b></em>' containment
     * reference list. The list contents are of type
     * {@link org.eclipse.sirius.tests.sample.scxml.ScxmlDatamodelType}. <!--
     * begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Datamodel</em>' containment reference list
     * isn't clear, there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Datamodel</em>' containment reference list.
     * @see org.eclipse.sirius.tests.sample.scxml.ScxmlPackage#getScxmlStateType_Datamodel()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData=
     *        "kind='element' name='datamodel' namespace='##targetNamespace' group='#ScxmlStateMix:0'"
     * @generated
     */
    EList<ScxmlDatamodelType> getDatamodel();

    /**
     * Returns the value of the '<em><b>Invoke</b></em>' containment reference
     * list. The list contents are of type
     * {@link org.eclipse.sirius.tests.sample.scxml.ScxmlInvokeType}. <!--
     * begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Invoke</em>' containment reference list isn't
     * clear, there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Invoke</em>' containment reference list.
     * @see org.eclipse.sirius.tests.sample.scxml.ScxmlPackage#getScxmlStateType_Invoke()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData=
     *        "kind='element' name='invoke' namespace='##targetNamespace' group='#ScxmlStateMix:0'"
     * @generated
     */
    EList<ScxmlInvokeType> getInvoke();

    /**
     * Returns the value of the '<em><b>Any</b></em>' attribute list. The list
     * contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Any</em>' attribute list isn't clear, there
     * really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Any</em>' attribute list.
     * @see org.eclipse.sirius.tests.sample.scxml.ScxmlPackage#getScxmlStateType_Any()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry"
     *        many="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData=
     *        "kind='elementWildcard' wildcards='##other' name=':11' processing='lax' group='#ScxmlStateMix:0'"
     * @generated
     */
    FeatureMap getAny();

    /**
     * Returns the value of the '<em><b>Id</b></em>' attribute. <!--
     * begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Id</em>' attribute isn't clear, there really
     * should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Id</em>' attribute.
     * @see #setId(String)
     * @see org.eclipse.sirius.tests.sample.scxml.ScxmlPackage#getScxmlStateType_Id()
     * @model id="true" dataType="org.eclipse.emf.ecore.xml.type.ID"
     *        extendedMetaData="kind='attribute' name='id'"
     * @generated
     */
    String getId();

    /**
     * Sets the value of the '
     * {@link org.eclipse.sirius.tests.sample.scxml.ScxmlStateType#getId
     * <em>Id</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Id</em>' attribute.
     * @see #getId()
     * @generated
     */
    void setId(String value);

    /**
     * Returns the value of the '<em><b>Initial1</b></em>' attribute. <!--
     * begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Initial1</em>' attribute isn't clear, there
     * really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Initial1</em>' attribute.
     * @see #setInitial1(List)
     * @see org.eclipse.sirius.tests.sample.scxml.ScxmlPackage#getScxmlStateType_Initial1()
     * @model dataType="org.eclipse.emf.ecore.xml.type.IDREFS" many="false"
     *        extendedMetaData="kind='attribute' name='initial'"
     * @generated
     */
    List<String> getInitial1();

    /**
     * Sets the value of the '
     * {@link org.eclipse.sirius.tests.sample.scxml.ScxmlStateType#getInitial1
     * <em>Initial1</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @param value
     *            the new value of the '<em>Initial1</em>' attribute.
     * @see #getInitial1()
     * @generated
     */
    void setInitial1(List<String> value);

    /**
     * Returns the value of the '<em><b>Any Attribute</b></em>' attribute list.
     * The list contents are of type
     * {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}. <!-- begin-user-doc
     * -->
     * <p>
     * If the meaning of the '<em>Any Attribute</em>' attribute list isn't
     * clear, there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Any Attribute</em>' attribute list.
     * @see org.eclipse.sirius.tests.sample.scxml.ScxmlPackage#getScxmlStateType_AnyAttribute()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry"
     *        many="true" extendedMetaData=
     *        "kind='attributeWildcard' wildcards='##other' name=':14' processing='lax'"
     * @generated
     */
    FeatureMap getAnyAttribute();

} // ScxmlStateType
