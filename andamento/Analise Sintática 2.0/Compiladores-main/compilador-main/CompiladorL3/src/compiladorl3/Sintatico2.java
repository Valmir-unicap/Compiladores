package compiladorl3;
/*
 * @author valmir
 */
public class Sintatico2 {
    private Lexico lexico;
    private Token token;
    
    public Sintatico2(Lexico lexico){
        this.lexico= lexico;
    }
    
    public void inicializacao(){
        this.token= this.lexico.nextToken();
        if(!token.getLexema().equals("int")){
            throw new RuntimeException("Faltou implementar o main");
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
        if(this.token.getTipo() == Token.TIPO_FIM_CODIGO){
            System.out.println("Compilação efetuada! @Equipe Dev - Valmir Júnior / Guilherme Belian");
        }else{
            throw new RuntimeException("Erro próximo do fim do programa.");
        }
    }
    
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
    
    private void CS(){
        if((this.token.getTipo() == Token.TIPO_IDENTIFICADOR) || this.token.getLexema().equals("int") || this.token.getLexema().equals("float") || this.token.getLexema().equals("char")){
            this.declaracao_atribuicao();
            this.CS();
            this.condicional();
        }else{
            
        }
    }
    
    private void declaracao_atribuicao(){
        if(this.token.getTipo() == Token.TIPO_IDENTIFICADOR){
            this.ATRIBUICAO();
        }else if(this.token.getLexema().equals("int") || this.token.getLexema().equals("float") || this.token.getLexema().equals("char")){
            this.DECLARACAO();
        }else{
            throw new RuntimeException("Erro, esperava-se que você declarasse um comando próximo de: "+this.token.getLexema());
        }
    }
    
    private void DECLARACAO(){
            if(!(this.token.getLexema().equals("int") || this.token.getLexema().equals("float") || this.token.getLexema().equals("char"))){
                throw new RuntimeException("Erro na declaração de variavel. "+" Você errou próximo de: "+this.token.getLexema());
            }
            this.token= this.lexico.nextToken();
            if(this.token.getTipo() != Token.TIPO_IDENTIFICADOR){
                 throw new RuntimeException("Erro na declaração de variavel. "+" Você errou próximo de: "+this.token.getLexema());
            }
            this.token = this.lexico.nextToken();
            if(!this.token.getLexema().equalsIgnoreCase(";")){
                 throw new RuntimeException("Erro na declaração de variavel. "+" Você errou próximo de: "+this.token.getLexema());
            }
        this.token = this.lexico.nextToken();
    }
    
    private void ATRIBUICAO(){
        if(this.token.getTipo() != Token.TIPO_IDENTIFICADOR){
            throw new RuntimeException("Erro de atribuição! Próximo de: "+this.token.getLexema());
        }
        this.token = this.lexico.nextToken();
        if(this.token.getTipo() != Token.TIPO_OPERADOR_ATRIBUICAO){
            throw new RuntimeException("Erro de atribuição! Próximo de: "+this.token.getLexema());
        }
        this.token = this.lexico.nextToken();
        this.E();
        if(!this.token.getLexema().equals(";")){
            throw new RuntimeException("Erro! Você não colocou ; / Próximo de: "+this.token.getLexema());
        }
        this.token = this.lexico.nextToken();
    }
      
    private void E(){
        this.T();
        this.El();
    }
    
    private void El(){
        if(this.token.getTipo() == Token.TIPO_OPERADOR_ARITMETICO){
            this.operador();
            this.T();
            this.El();
        }else{
            //OLHA AQUI
        }     
    }
    
    private void T(){
        if(this.token.getTipo() == Token.TIPO_IDENTIFICADOR || this.token.getTipo() == Token.TIPO_INTEIRO || this.token.getTipo() == Token.TIPO_REAL || this.token.getTipo() == Token.TIPO_CHAR){
            this.token=this.lexico.nextToken();
        }else{
            throw new RuntimeException("Erro! Era para ser um identificador " + "ou numero proximo de "+ this.token.getLexema());
        }        
    }
    
    private void operador(){
        if(this.token.getTipo() == Token.TIPO_OPERADOR_ARITMETICO){
            this.token=this.lexico.nextToken();
        }else{
            throw new RuntimeException("Erro! Era para ser um operador" + " aritmetico(+/-/*/%/) proximo de "+ this.token.getLexema());
        }      
    }
    
    public void seNao(){
        if(this.token.getTipo() == Token.TIPO_PALAVRA_RESERVADA && token.getLexema().compareTo("else")==0){
            this.token= this.lexico.nextToken();
        }else{
            throw new RuntimeException("Erro! Era para ser palavra reservada = " + " else "+ this.token.getLexema());
        }
    }
    
    private void condicional(){
        if(this.token.getTipo() == Token.TIPO_PALAVRA_RESERVADA && token.getLexema().compareTo("if")==0){
            this.token= this.lexico.nextToken();
            this.abreParenteses();
            this.relacional();
            this.fechaParenteses();
            this.abreChave();
            this.declaracao_atribuicao();
            this.fechaChave();
            this.seNao();
            this.abreChave();
            this.declaracao_atribuicao();
            this.fechaChave();
        }
    }    
    
    private void abreChave(){
        if(this.token.getTipo() == Token.TIPO_CARACTER_ESPECIAL && token.getLexema().compareTo("{")==0){
            this.token= this.lexico.nextToken();
        }else{
            throw new RuntimeException("Erro! Era para ser abertura de" + " { "+ this.token.getLexema());
        }
    }
    
    private void fechaChave(){
         if(this.token.getTipo() == Token.TIPO_CARACTER_ESPECIAL && token.getLexema().compareTo("}")==0){
            this.token= this.lexico.nextToken();
        }else{
            throw new RuntimeException("Erro! Era para fechar " + " { "+ this.token.getLexema());
        }
    }
    
    private void abreParenteses(){
        if(this.token.getTipo() == Token.TIPO_CARACTER_ESPECIAL && token.getLexema().compareTo("(")==0){
            this.token= this.lexico.nextToken();
        }else{
            throw new RuntimeException("Erro! Era para ser abertura de" + " ( "+ this.token.getLexema());
        }
    }
    
    private void fechaParenteses(){
        if(this.token.getTipo() == Token.TIPO_CARACTER_ESPECIAL && token.getLexema().compareTo(")")==0){
            this.token= this.lexico.nextToken();
        }else{
            throw new RuntimeException("Erro! Era para ser fechamento de" + " ) "+ this.token.getLexema());
        }
    }
    
    private void relacional(){
        if(this.token.getTipo() == Token.TIPO_OPERADOR_RELACIONAL && token.getLexema().compareTo("<=")==0 || token.getLexema().compareTo(">=")==0 || token.getLexema().compareTo("==")==0 || token.getLexema().compareTo("<>")==0){
            this.token= this.lexico.nextToken();
        }else{
            throw new RuntimeException("Erro! Era para ser operador relacional de" + " (==/<=/>=/<>) "+ this.token.getLexema());
        }
    }
    
    
}