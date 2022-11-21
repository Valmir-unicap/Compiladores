/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package compiladorl3;

/**
 *
 * @author aluno
 */
public class Semantico {
    private String nome;
    private String tipo;
    private int escopo;
    
    public Semantico(String nome, String tipo, int escopo){
        this.nome = "";
        this.tipo = "";
        this.escopo= 0;
    }     
    public void setNome(String nome){
        this.nome= nome;
    }
    public String getNome(){
        return this.nome;
    }
    public void setTipo(String tipo){
        this.tipo = tipo;
    }
    public String getTipo(){
        return this.tipo;
    }
    public void setEscopo(int escopo){
        this.escopo= escopo;
    }
    public int getEscopo(){
        return this.escopo;
    }
}
