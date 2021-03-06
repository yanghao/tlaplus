package org.lamport.tla.toolbox.tool.tlc.ui.wizard;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.lamport.tla.toolbox.tool.tlc.ui.util.FormHelper;

/**
 * A page with a simple field for formula editing
 * @author Simon Zambrovski
 * @version $Id$
 */
public class FormulaWizardPage extends WizardPage
{
    private SourceViewer sourceViewer;
    private Document document;

    public FormulaWizardPage(String action, String description)
    {
        super("FormulaWizardPage");
        setTitle(action);
        setDescription(description);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent)
    {
        Composite container = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout(1, false);
        container.setLayout(layout);

        sourceViewer = FormHelper.createSourceViewer(container, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);

        // SWT.FILL causes the text field to expand and contract
        // with changes in size of the dialog window.
        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd.heightHint = 200;
        gd.widthHint = 400;
        gd.grabExcessHorizontalSpace = true;
        gd.grabExcessVerticalSpace = true;

        StyledText control = sourceViewer.getTextWidget();
        control.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e)
            {
                getContainer().updateButtons();
            }

        });
        control.setEditable(true);
        control.setLayoutData(gd);

        if (this.document == null)
        {
            this.document = new Document();
        }
        sourceViewer.setDocument(this.getDocument());
        setControl(container);
    }

    public Document getDocument()
    {
        return this.document;
    }

    public void setDocument(Document document)
    {
        this.document = document;
    }

}
