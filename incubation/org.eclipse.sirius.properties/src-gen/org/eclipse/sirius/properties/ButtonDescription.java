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

import org.eclipse.sirius.viewpoint.description.tool.InitialOperation;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Button Description</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>
 * {@link org.eclipse.sirius.properties.ButtonDescription#getButtonLabelExpression
 * <em>Button Label Expression</em>}</li>
 * <li>
 * {@link org.eclipse.sirius.properties.ButtonDescription#getInitialOperation
 * <em>Initial Operation</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.sirius.properties.PropertiesPackage#getButtonDescription()
 * @model
 * @generated
 */
public interface ButtonDescription extends WidgetDescription {
    /**
     * Returns the value of the '<em><b>Button Label Expression</b></em>'
     * attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Button Label Expression</em>' attribute isn't
     * clear, there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Button Label Expression</em>' attribute.
     * @see #setButtonLabelExpression(String)
     * @see org.eclipse.sirius.properties.PropertiesPackage#getButtonDescription_ButtonLabelExpression()
     * @model dataType=
     *        "org.eclipse.sirius.viewpoint.description.InterpretedExpression"
     * @generated
     */
    String getButtonLabelExpression();

    /**
     * Sets the value of the '
     * {@link org.eclipse.sirius.properties.ButtonDescription#getButtonLabelExpression
     * <em>Button Label Expression</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Button Label Expression</em>'
     *            attribute.
     * @see #getButtonLabelExpression()
     * @generated
     */
    void setButtonLabelExpression(String value);

    /**
     * Returns the value of the '<em><b>Initial Operation</b></em>' containment
     * reference. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Initial Operation</em>' containment reference
     * isn't clear, there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Initial Operation</em>' containment
     *         reference.
     * @see #setInitialOperation(InitialOperation)
     * @see org.eclipse.sirius.properties.PropertiesPackage#getButtonDescription_InitialOperation()
     * @model containment="true" required="true"
     * @generated
     */
    InitialOperation getInitialOperation();

    /**
     * Sets the value of the '
     * {@link org.eclipse.sirius.properties.ButtonDescription#getInitialOperation
     * <em>Initial Operation</em>}' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Initial Operation</em>' containment
     *            reference.
     * @see #getInitialOperation()
     * @generated
     */
    void setInitialOperation(InitialOperation value);

} // ButtonDescription
