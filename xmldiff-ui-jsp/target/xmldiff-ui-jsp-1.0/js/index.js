function processForm(form) {

    if(document.pressed == 'Help')
    {
        document.xmlDiffForm.action ="HelpService";
    }
    else if(document.pressed == 'Compare')
    {
        document.xmlDiffForm.action ="CompareService";
    }
}
