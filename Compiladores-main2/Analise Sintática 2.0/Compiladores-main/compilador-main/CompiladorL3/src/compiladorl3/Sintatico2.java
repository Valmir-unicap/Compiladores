package compiladorl3;
/*
 * @author aluno
 */
public class Sintatico2 {
    private Lexico lexico;
    private Token token;
    
    public Sintatico2(Lexico lexico){
        this.lexico= lexico;
    }
    
    public void S(){
        this.token= this.lexico.nextToken();
        if(!token.getLexema().equals("int")){
            throw new RuntimeException("Faltou implementar o int");
        }
        this.token=this.lexico.nextToken();
        if(!token.getLexema().equals("main")){
            throw new RuntimeException("Faltou implementar o main");
        }
        this.token=this.lexico.nextToken();
        if(!token.getLexema().equals("(")){
            throw new RuntimeException("Você esqueceu de abrir ( ");
        }
        this.token= this.lexico.nextToken();
        if(!token.getLexema().equals(")")){
            throw new RuntimeException("Você esqueceu de fechar ) ");
        }
        this.token= this.lexico.nextToken();

        this.B();

        /* Após quebrar toda a pilha de recursão, sai do B() e checa
         * se o token atual não é nulo e é do tipo fim de código.
         * Não sendo nulo e sendo fim de código, finaliza com mensagem de êxito.
         * Senão, lança exceção. */
        if(this.token != null && this.token.getTipo() == Token.TIPO_FIM_CODIGO){
            System.out.println("Compilação efetuada! @Equipe Dev - Valmir Júnior / Guilherme Belian");
        }else{
            throw new RuntimeException("Erro! próximo do fim do programa.");
        }
    }

    /* Abertura do bloco do main.
     * Checa a abertura da primeira chave depois de main() e só retorna no final do código
     * após toda a pilha de recursão do CS ser finalizada para checar se o token é o fecha chave
     * correspondente ao fechamento deste bloco. Se for, pega o próximo token. Senão, lança exceção. */
    private void B(){
        if(!this.token.getLexema().equals("{")){
            throw new RuntimeException("Erro, esperava "+"{"+" / próximo de  "+this.token.getLexema());
        }
        this.token= this.lexico.nextToken();
        this.CS();
        if(!this.token.getLexema().equals("}")){
            throw new RuntimeException("Você esqueceu de fechar "+"}"+ " / próximo de "+this.token.getLexema());
        }
        this.token= this.lexico.nextToken();
    }

    /* Método recursivo principal.
     * Checa todos os tipos e lexemas de tokens e chama seus respectivos métodos. */
    private void CS(){
        if(
                this.token.getLexema().equals("int") ||
                this.token.getLexema().equals("float") ||
                this.token.getLexema().equals("char")){
            this.C();
            this.CS();
        }else if(this.token.getTipo() == Token.TIPO_OPERADOR_ARITMETICO){
            this.E();
            this.CS();
        }else if(this.token.getTipo() == Token.TIPO_PALAVRA_RESERVADA){
            this.PL();
            this.CS();
        }else if(this.token.getTipo() == Token.TIPO_INTEIRO){
            this.T();
            this.CS();
        }else if(this.token.getLexema().equals(";")){
            this.SC();
            this.CS();
        }else if(this.token.getTipo() == Token.TIPO_IDENTIFICADOR){
            this.E();
            this.CS();
        }else if(this.token.getTipo() == Token.TIPO_CHAR){
            this.T();
            this.CS();
        }else if(this.token.getTipo() == Token.TIPO_REAL){
            this.T();
            this.CS();
        }
    }
    
    private void C(){
        if(this.token.getTipo() == Token.TIPO_IDENTIFICADOR){
            this.ATRIBUICAO();
        }else if(this.token.getLexema().equals("int") ||
                this.token.getLexema().equals("float") ||
                this.token.getLexema().equals("char")){

            this.DECLARACAO();
        }else{
            throw new RuntimeException("Erro, esperava-se que você declarasse um comando próximo de: "+this.token.getLexema());
        }
    }
    
    private void DECLARACAO(){
            if(!(this.token.getLexema().equals("int") ||
                    this.token.getLexema().equals("float") ||
                    this.token.getLexema().equals("char"))){

                throw new RuntimeException("Erro! na declaração de variavel. "+" Você errou próximo de: "+this.token.getLexema());
            }
            this.token= this.lexico.nextToken();
            if(this.token.getTipo() != Token.TIPO_IDENTIFICADOR){
                 throw new RuntimeException("Erro! na declaração de variavel. "+" Você errou próximo de: "+this.token.getLexema());
            }
            this.token = this.lexico.nextToken();
            if(this.token.getTipo() != Token.TIPO_OPERADOR_ATRIBUICAO && !this.token.getLexema().equalsIgnoreCase(";")){
                 throw new RuntimeException("Erro! na declaração de variavel. "+" Você errou próximo de: "+this.token.getLexema());
            }
        this.token = this.lexico.nextToken();
    }
    
    private void ATRIBUICAO(){
        if(this.token.getTipo() != Token.TIPO_IDENTIFICADOR){
            throw new RuntimeException("Erro de atribuição! Próximo de: "+this.token.getLexema());
        }
        // this.token = this.lexico.nextToken();
        if(this.token.getTipo() == Token.TIPO_OPERADOR_RELACIONAL){
            this.token= this.lexico.nextToken();
            return;
        }
        if(this.token.getTipo() == Token.TIPO_CARACTER_ESPECIAL){
            this.token= this.lexico.nextToken();
            return;
        }
        if(this.token.getTipo() == Token.TIPO_OPERADOR_ARITMETICO){
            this.token= this.lexico.nextToken();
            return;
        }
        if(this.token.getTipo() == Token.TIPO_PALAVRA_RESERVADA){
            this.token = this.lexico.nextToken();
            return;
        }
        if(this.token.getTipo() != Token.TIPO_OPERADOR_ATRIBUICAO){
            throw new RuntimeException("Erro de atribuição! Próximo de: "+this.token.getLexema());
        }
        this.token = this.lexico.nextToken();
        this.E();
        this.token = this.lexico.nextToken();
    }
      
    private void E(){
        if(this.token.getTipo() == Token.TIPO_IDENTIFICADOR ||
                this.token.getLexema().equals("int") ||
                this.token.getLexema().equals("float")){

            this.T();

            if(this.token.getTipo() == Token.TIPO_OPERADOR_ARITMETICO){
                this.El();
            }
            if(this.token.getTipo() == Token.TIPO_OPERADOR_ATRIBUICAO){
                this.AV();
            }
        }
    }
    
    private void El(){
        if(this.token.getTipo() == Token.TIPO_OPERADOR_ARITMETICO){
            this.OP();
            this.T();
            this.El();
        }else{
        }     
    }

    /* Método criado para verificar se o token atual é do tipo operador de atribuição (=).
     * Se for, executa procedimento de atribuição em uma variável. */
    private void AV() {
        if (this.token.getTipo() == Token.TIPO_OPERADOR_ATRIBUICAO) {

            /* Pega próximo token e checa se não é identificador ou número.
             * Não sendo nenhum dos esperados, lança exceção. */
            this.token = this.lexico.nextToken();
            if (this.token.getTipo() != Token.TIPO_IDENTIFICADOR
                    && this.token.getTipo() != Token.TIPO_INTEIRO
                    && this.token.getTipo() != Token.TIPO_REAL
                    && this.token.getTipo() != Token.TIPO_CHAR) {
                throw new RuntimeException("Erro na atribuição de variável. Era pra ter colocado" +
                        " um identificador ou número ou char perto de " + this.token.getLexema());
            }

            /* Pega próximo token e checa se é operador aritmetico ou
             * identificador ou número ou ;. Não sendo nenhum dos esperados,
             * lança exceção. */
            this.token = this.lexico.nextToken();
            if (this.token.getTipo() == Token.TIPO_OPERADOR_ARITMETICO
                    || this.token.getTipo() == Token.TIPO_IDENTIFICADOR
                    || this.token.getTipo() == Token.TIPO_INTEIRO
                    || this.token.getTipo() == Token.TIPO_REAL
                    || this.token.getTipo() == Token.TIPO_CHAR
                    || this.token.getLexema().equals(";")) {
                this.token = this.lexico.nextToken();
            } else {
                throw new RuntimeException("Erro na atribuição de variável. Era pra ter colocado" +
                        " um operador aritmetico (+,-,*,/) ou identificador ou número" +
                        " ou char ou finalizar a atribuição com ; perto de " + this.token.getLexema());
            }
        }
    }

    private void T(){
        if(this.token.getTipo() == Token.TIPO_IDENTIFICADOR ||
                this.token.getTipo() == Token.TIPO_INTEIRO ||
                this.token.getTipo() == Token.TIPO_REAL ||
                this.token.getTipo() == Token.TIPO_CHAR){

            this.token=this.lexico.nextToken();
        }else{
            throw new RuntimeException("Erro! Era para ser um identificador " + "ou numero proximo de "+ this.token.getLexema());
        }        
    }
    
    private void OP(){
        if(this.token.getTipo() == Token.TIPO_OPERADOR_ARITMETICO){
            this.token=this.lexico.nextToken();
        }else{
            throw new RuntimeException("Erro! Era para ser um operador" + " aritmetico(+/-/*/%/) proximo de "+ this.token.getLexema());
        }      
    }

    /* Método criado primeiramente para checar se o token atual é um caractere especial (SC = Special Char).
     * Todavia, como a checagem de alguns caracteres foi feita separadamente, este método agora apenas checa
     * se o token é um ponto e vírgula (;). */
    private void SC() {
        if (this.token.getLexema().equals(";")) {
            this.token = this.lexico.nextToken();
        } else {
            throw new RuntimeException("Tu esqueceu do ; perto de " + this.token.getLexema());
        }
    }

    /* Método criado para checar se o token é uma palavra reservada
     * if | else | do | while | main | int | float | char
     *
     * Sendo o lexema do token "if", "else" ou "while", executa procedimento para checar
     * se o comando foi feito de maneira correta.
     * Caso não seja if nem else nem while, mas seja palavra reservada, pega o próximo token.
     * Não sendo palavra reservada, lança exceção.
     *
     * Se if ou while -> checa se o próximo token é (, depois parte para a verificação do escopo em recursão.
     * Ao sair da recursão, checa se o próximo token é ). Sendo o token ")", checa se o próximo token é {.
     * Sendo {, parte para a verificação do bloco em recursão. Saindo, verifica se o token é }.
     *
     * Se else -> checa se o próximo token é {, depois parte para verificação do bloco em recursão.
     * Ao sair da recursão, checa se o token é }.*/
    private void PL() {
        if (this.token.getLexema().equals("if")
                || this.token.getLexema().equals("while")) {
            this.token = this.lexico.nextToken();
            this.AP(); // Abre parêntese
            this.FP(); // Fecha parêntese
            this.AC(); // Abre chave
            this.FC(); // Fecha chave
    } else if (this.token.getLexema().equals("}") && this.token.getLexema().equals("else")) {
            // } else if (this.token.getLexema().equals("else")) {
            this.token = this.lexico.nextToken();

            /* Se o próximo token tiver o lexema "if", reconhecer comando
             * else if (cmd | decl) { cmds | decl }.
             * No momento não funcional, reconhecendo apenas comando "else". */
            if (this.token.getLexema().equals("if")) {
                this.lexico.nextToken();
                this.AP();
                this.FP();
                this.AC();
                this.FC();
            } else if (this.token.getLexema().equals("{")) {
                this.AC();
                this.FC();
            }
        } else if (this.token.getTipo() == Token.TIPO_PALAVRA_RESERVADA) {
            this.token = this.lexico.nextToken();
        } else {
            throw new RuntimeException("Era pra tu ter colocado uma palavra reservada "
                    + "(main | if | else | int | float | char | do | for | while) perto de " +
                    this.token.getLexema());
        }
    }

    /* Método criado para checar abertura de parêntese e conteúdo dentro do escopo. */
    private void AP() {
        if (this.token.getLexema().equals("(")) {
            this.token = this.lexico.nextToken();

            /* Checa se lexema é int | float | char e se o próximo é identificador
             * (declaração de variável dentro de escopo do if ou while). */
            if (this.token.getLexema().equals("int")
                    || this.token.getLexema().equals("float")
                    || this.token.getLexema().equals("char")) {
                this.token = this.lexico.nextToken();
                if (this.token.getTipo() != Token.TIPO_IDENTIFICADOR) {
                    throw new RuntimeException("Errasse na declaração de variável" +
                            " dentro do escopo do if ou while. Era pra ter colocado" +
                            " um identificador perto de " + this.token.getLexema());
                } else {
                    this.token = this.lexico.nextToken();
                }
            }

            /* Checa se o token é do tipo inteiro ou float e chama o próximo token. */
            else if (this.token.getTipo() == Token.TIPO_INTEIRO
                    || this.token.getTipo() == Token.TIPO_REAL
                    || this.token.getTipo() == Token.TIPO_IDENTIFICADOR) {
                this.token = this.lexico.nextToken();
            }

            /* Checa se o token é do tipo operador aritmetico, atribuição ou relacional. */
            if (this.token.getTipo() == Token.TIPO_OPERADOR_ARITMETICO
                    || this.token.getTipo() == Token.TIPO_OPERADOR_ATRIBUICAO
                    || this.token.getTipo() == Token.TIPO_OPERADOR_RELACIONAL) {
                this.token = this.lexico.nextToken();
            } else {
                throw new RuntimeException("Erro! Na operação de variável" +
                        " dentro do escopo do if ou while.\nEra pra ter colocado um" +
                        " operador aritmético (+,-,*,/), operador de atribuição (=) ou" +
                        " operador relacional (<, <=, >, >=, !=, ==) perto de " + this.token.getLexema());
            }

            if (this.token.getTipo() == Token.TIPO_IDENTIFICADOR
                    || this.token.getTipo() == Token.TIPO_INTEIRO
                    || this.token.getTipo() == Token.TIPO_REAL
                    || this.token.getTipo() == Token.TIPO_CHAR) {
                this.token = this.lexico.nextToken();
            } else {
                throw new RuntimeException("Errasse na operação de variável" +
                        " dentro do escopo do if ou while.\nEra pra ter colocado um" +
                        " identificador ou número ou char perto de " + this.token.getLexema());
            }

        } else {
            throw new RuntimeException("Ei boy, tu não abriu o parêntese do teu if/while/else if" +
                    " perto de " + this.token.getLexema() + " visse.");
        }
    }

    /* Método criado para checar fechamento de parêntese. */
    private void FP() {
        if (this.token.getLexema().equals(")")) {
            this.token = this.lexico.nextToken();
        } else {
            throw new RuntimeException("Aguarde o processo, esqueceu de fechar o parêntese do " +
                    "if ou while antes de " + this.token.getLexema());
        }
    }

    /* Método criado para checar abertura de chave. */
    private void AC() {
        if (this.token.getLexema().equals("{")) {
            this.token = this.lexico.nextToken();
            this.CS();
        } else {
            throw new RuntimeException("Esqueceu de abrir chaves perto de " + this.token.getLexema());
        }
    }

    /* Método criado para checar fechamento de chave. */
    private void FC() {
        if (this.token.getLexema().equals("}")) {
            this.token = this.lexico.nextToken();
        } else {
            throw new RuntimeException("Esqueceu de fechar chaves perto de " + this.token.getLexema());
        }
    }
}
