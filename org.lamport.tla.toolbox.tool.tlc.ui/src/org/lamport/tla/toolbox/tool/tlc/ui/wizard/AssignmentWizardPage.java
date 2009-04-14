package org.lamport.tla.toolbox.tool.tlc.ui.wizard;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.lamport.tla.toolbox.tool.tlc.model.Assignment;
import org.lamport.tla.toolbox.tool.tlc.model.TypedSet;

/**
 * @author Simon Zambrovski
 * @version $Id$
 */
public class AssignmentWizardPage extends WizardPage
{
    private LabeledListComposite paramComposite;
    private SourceViewer source;
    private Button optionModelValue;
    private final int fieldFlags;
    private Button optionSetModelValues;
    private Button makeSymmetricalSet;
    private Button optionOrdinaryValue;
    // selection adapter reacting on the choice
    private SelectionListener optionSelectionAdapter = new SelectionAdapter() {
        public void widgetSelected(SelectionEvent e)
        {
            boolean modelValueSelected = optionModelValue.getSelection();
            boolean modelValueSetSelected = optionSetModelValues.getSelection();


            if (modelValueSelected) 
            {
                source.getTextWidget().setBackground(getControl().getBackground());
            } else {
                source.getTextWidget().setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
            }
            source.getControl().setEnabled(!modelValueSelected);
            
            makeSymmetricalSet.setEnabled(modelValueSetSelected);

            getContainer().updateButtons();
        }
    };

    public AssignmentWizardPage(String action, String description, int fieldFlags)
    {
        super("AssignmentWizardPage");
        this.fieldFlags = fieldFlags;
        setTitle(action);
        setDescription(description);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent)
    {
        Composite container = new Composite(parent, SWT.NULL);
        container.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, true));
        GridLayout layout = new GridLayout(2, false);
        container.setLayout(layout);
        GridData gd;

        paramComposite = new LabeledListComposite(container, getAssignment().getLabel(), getAssignment().getParams());
        gd = new GridData(SWT.LEFT, SWT.TOP, false, true);
        paramComposite.setLayoutData(gd);

        source = new SourceViewer(container, null, null, false, SWT.BORDER | SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        SourceViewerConfiguration configuration = new SourceViewerConfiguration();
        source.configure(configuration);
        source.setDocument(new Document(getAssignment().getRight()));

        StyledText styledText = source.getTextWidget();
        styledText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e)
            {
                getContainer().updateButtons();
            }

        });
        styledText.setBackgroundMode(SWT.INHERIT_FORCE);
        styledText.setEditable(true);

        gd = new GridData(SWT.RIGHT, SWT.TOP, true, true);
        gd.minimumWidth = 500;
        gd.minimumHeight = 100;
        styledText.setLayoutData(gd);

        // constant, no parameters
        if (!paramComposite.hasParameters())
        {
            // both bits set, make a radio list
            if ((fieldFlags & AssignmentWizard.MAKE_MODEL_VALUE & AssignmentWizard.MAKE_SET_MODEL_VALUE) == (AssignmentWizard.MAKE_MODEL_VALUE & AssignmentWizard.MAKE_SET_MODEL_VALUE))
            {
                // ordinary value option
                optionOrdinaryValue = new Button(container, SWT.RADIO);
                optionOrdinaryValue.setText("Ordinary assignment");
                gd = new GridData(SWT.LEFT, SWT.TOP, false, false);
                gd.horizontalSpan = 2;
                optionOrdinaryValue.setLayoutData(gd);

                // make a model value
                optionModelValue = new Button(container, SWT.RADIO);
                optionModelValue.setText("Model value");

                gd = new GridData(SWT.LEFT, SWT.TOP, false, false);
                gd.horizontalSpan = 2;
                optionModelValue.setLayoutData(gd);

                // make a set of model values
                optionSetModelValues = new Button(container, SWT.RADIO);
                optionSetModelValues.setText("Set of model values");
                gd = new GridData(SWT.LEFT, SWT.TOP, false, false);
                gd.horizontalSpan = 2;
                optionSetModelValues.setLayoutData(gd);

                // option to make a set symmetrical
                makeSymmetricalSet = new Button(container, SWT.CHECK);
                makeSymmetricalSet.setText("Symmetrical");
                gd = new GridData(SWT.LEFT, SWT.TOP, false, false);
                gd.horizontalSpan = 2;
                gd.horizontalIndent = 10;
                makeSymmetricalSet.setLayoutData(gd);

                // install listeners
                optionOrdinaryValue.addSelectionListener(optionSelectionAdapter);
                optionModelValue.addSelectionListener(optionSelectionAdapter);
                optionSetModelValues.addSelectionListener(optionSelectionAdapter);

                
                // set the value from the assignment object
                if (getAssignment().isModelValue())
                {
                    if (getAssignment().getLabel().equals(getAssignment().getRight()))
                    {
                        optionModelValue.setSelection(getAssignment().isModelValue());
                        source.getTextWidget().setBackground(container.getBackground());
                    } else {
                        optionSetModelValues.setSelection(getAssignment().isModelValue());
                    }
                } else {
                    optionOrdinaryValue.setSelection(true);
                }

            }
        }

        setControl(container);
    }

    /**
     * The assignment with modified params and right part 
     * @return
     */
    public Assignment getAssignment()
    {
        return ((AssignmentWizard) getWizard()).getFormula();
    }

    public boolean finish()
    {
        return false;
    }

    public void dispose()
    {
        String rightSide = source.getDocument().get();
        // if the model value(s) option exist 
        if (optionModelValue != null && optionSetModelValues != null)
        {
            this.getAssignment().setModelValue(optionModelValue.getSelection() || optionSetModelValues.getSelection());

            // handling the option selected
            if (optionModelValue.getSelection()) 
            {
                // model value
                this.getAssignment().setRight(this.getAssignment().getLabel());
            } else if (optionSetModelValues.getSelection()) 
            {
                // set of model values
                // normalize the right side
                TypedSet set = TypedSet.parseSet(rightSide);
                this.getAssignment().setRight(set.toString());
            } else {
                // ordinary assignment (with no parameters)
                this.getAssignment().setRight(rightSide);
            }
            
        } else {
            // no options - e.G. definition override, or constant with multiple parameters
            this.getAssignment().setRight(rightSide);
        }

        // if there are parameters, set them
        if (paramComposite.hasParameters())
        {
            this.getAssignment().setParams(paramComposite.getValues());
        }
        super.dispose();
    }

    /*
     * Show the next page ( for typing of model values sets )
     * @see org.eclipse.jface.wizard.WizardPage#getNextPage()
     */
    public IWizardPage getNextPage()
    {
        if (isTypeInputPossible())
        {
            return super.getNextPage();
        }
        return null;
    }
    
    protected boolean isTypeInputPossible()
    {
        // only a set of model values can be typed
        if (optionSetModelValues == null || !optionSetModelValues.getSelection())
        {
            return false;
        }
        String set = source.getDocument().get();
        TypedSet parsedSet = TypedSet.parseSet(set.trim());

        return (parsedSet.getType() == null);
    }

    
    public boolean isCurrentPage()
    {
        return super.isCurrentPage();
    }

}
