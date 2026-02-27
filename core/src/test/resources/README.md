# Test Email Files

To properly run the `EmailParserSpec` tests, you need to provide your own `.eml` files in this directory, as the original files contain Personally Identifiable Information (PII) and cannot be committed to Git.

Please include two types of email files:

1.  **With Automated Content:** An email that **contains** the standard JustServe automated email footer. The filename for this file must include the word `with`.
    *   Example: `email-with-content.eml`

2.  **Without Automated Content:** An email that **does not contain** the standard JustServe automated email footer. The filename for this file must include the word `without`.
    *   Example: `email-without-content.eml`

The tests are designed to dynamically find and parse any `.eml` files in this directory and will assert different outcomes based on whether "with" or "without" is present in the filename.
