# inversed-deduction
This is a simple inversed deduction program implementation. It works on shops and products example. User inputs formulas known by knowledge base of the engine and then a question formula which will be checked by the app. If there is some information that leads us to some variable substitutions that fulfill given question formula, program returns those substitutions and success message. Otherwise, failure message is printed to user. <br />
#### Sample input and results
Knowledge base formulas: *offers(TOPAZ, MLEKO), producent(MLEKO,SANTE)* <br />
Sample question formula: *offers(TOPAZ, x) and producent(x,SANTE)* <br />
Result: *success,substitutions: {x=MLEKO}* </br>

## Used technologies and tools
* Java 8
* JUnit
* Maven
* Lombock
