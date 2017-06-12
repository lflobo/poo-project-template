# Criação da Tabela para o exemplo

```sql
CREATE TABLE carro (
  num_chassis INT          NOT NULL PRIMARY KEY AUTO_INCREMENT,
  marca       VARCHAR(100) NOT NULL,
  modelo      VARCHAR(100) NOT NULL,
  cc          INT          NOT NULL,
  tara        INT          NOT NULL
);
```
Ou seguindo a descrição na imagem:

![table-carro](https://raw.githubusercontent.com/lflobo/poo-project-template/master/doc/images/table-carro.png)

# Credênciais de acesso

Alterar o ficheiro `Main.java` [nesta linha](https://github.com/lflobo/poo-project-template/blob/master/src/main/java/pt/ipb/poo/projeto/Main.java#L22).