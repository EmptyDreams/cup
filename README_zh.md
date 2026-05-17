# CUP 中文用户手册

CUP（Creation of Useful Parsers）是一个面向 Java 的 LALR 解析器生成器。你用 `.cup` 文件描述语法，CUP 生成 Java 解析器和符号常量；如果启用 AST 功能，还会生成可遍历的语法树节点类。

本项目适合用于编译器、解释器、配置语言、DSL、静态分析工具等场景。文档面向使用者，重点说明如何编写规范、生成解析器、接入扫描器和使用 AST。

## 快速开始

### 准备工具

确保你可以运行 CUP，并且在编译、运行生成后的解析器时能引入 CUP runtime。下面的命令用 `java-cup.jar` 和 `java-cup-runtime.jar` 作为示例；如果你通过 Maven、Gradle 或其它方式集成，请替换为对应依赖。

### 生成解析器

```bash
java -jar java-cup.jar -parser CalcParser -symbols CalcSymbols -interface -destdir generated grammar.cup
```

CUP 会生成解析器类和符号常量类；启用 `-ast` 时，还会生成 AST 节点类。

## 使用流程

```mermaid
flowchart LR
  A["编写 .cup 文法"] --> B["准备 Scanner"]
  B --> C["运行 CUP 生成 Java 代码"]
  C --> D["编译生成代码和运行时库"]
  D --> E["调用 parser.parse()"]
  E --> F["得到解析结果或 AST"]
```

一个完整接入通常包含三部分：

1. `.cup` 文件：声明 token、非终结符、优先级和产生式。
2. 扫描器：把输入字符流转换成 CUP `Symbol`。
3. 业务代码：创建 parser，调用 `parse()`，读取结果。

## 第一个文法

下面是一个简单表达式求值器：

```cup
import java_cup.runtime.*;

terminal int NUMBER;
terminal PLUS, MINUS, TIMES, LPAREN, RPAREN, SEMI;

non terminal int expr;
non terminal line;

precedence left PLUS, MINUS;
precedence left TIMES;

line ::= expr:e SEMI {: System.out.println(e); :};

expr ::= expr:a PLUS expr:b   {: RESULT = a + b; :}
       | expr:a MINUS expr:b  {: RESULT = a - b; :}
       | expr:a TIMES expr:b  {: RESULT = a * b; :}
       | LPAREN expr:e RPAREN {: RESULT = e; :}
       | NUMBER:n             {: RESULT = n; :}
       ;
```

关键概念：

| 概念             | 说明                 |
|----------------|--------------------|
| `terminal`     | 扫描器返回的 token（即终结符） |
| `non terminal` | 文法中的非终结符           |
| `precedence`   | 解决表达式优先级和结合性       |
| `{: ... :}`    | 产生式动作，里面写 Java 代码  |
| `label:name`   | 给右侧符号加标签，动作里可读取它   |
| `RESULT`       | 当前产生式规约后的值         |

## CUP 文件结构

一个 `.cup` 文件通常按下面顺序组织：

```cup
package demo;
import java_cup.runtime.*;

class DemoParser;

parser code {: ... :};
action code {: ... :};
init with {: ... :};
scan with {: ... :};

terminal NUMBER, PLUS;
non terminal expr;

precedence left PLUS;

start with expr;

expr ::= ... ;
```

其中只有符号声明和产生式是必需的，其它部分按需要使用。

### 包、导入和类名

```cup
package com.example.parser;
import java.util.*;
class MiniParser;
```

`package` 和 `import` 会出现在生成的 Java 文件中。`class MiniParser;` 会把默认解析器类名设为 `MiniParser`，符号类名设为 `MiniParserSym`。

你也可以用命令行指定：

```bash
java -jar java-cup.jar -parser MiniParser -symbols MiniSymbols grammar.cup
```

### 用户代码块

| 写法                       | 用途                       |
|--------------------------|--------------------------|
| `parser code {: ... :};` | 给 parser 添加构造器、入口方法或错误处理 |
| `action code {: ... :};` | 给产生式动作共享辅助方法或字段          |
| `init with {: ... :};`   | 解析开始前执行初始化               |
| `scan with {: ... :};`   | 自定义如何读取下一个 token         |

常见用法：

```cup
init with {: ((scanner) getScanner()).init(); :};
scan with {: return getScanner().next_token(); :};
```

如果你使用生成 parser 的默认扫描器构造器，通常不需要写 `scan with`。

## 符号声明

终结符由扫描器返回：

```cup
terminal SEMI, PLUS;
terminal int NUMBER;
terminal String IDENT;
```

非终结符由产生式规约得到：

```cup
non terminal int expr;
non terminal AstNode stmt, program;
```

未写类型时，动作中的标签值按 `Object` 处理。如果某个符号只是语法标记、不需要业务值，扫描器可以返回不带值的 `Symbol`。

保留符号：

| 名称      | 说明          |
|---------|-------------|
| `EOF`   | 输入结束        |
| `error` | 错误恢复用 token |

## 产生式、动作和标签

基本写法：

```cup
expr ::= expr:left PLUS expr:right {: RESULT = left + right; :}
       | NUMBER:n                 {: RESULT = n; :}
       ;
```

标签会生成两个常用变量：

| 变量                          | 说明                      |
|-----------------------------|-------------------------|
| `left`、`right`、`n`          | 对应符号的值                  |
| `leftSym`、`rightSym`、`nSym` | 对应的完整 `Symbol`，可读取位置等信息 |

`RESULT` 是左侧非终结符的值。比如 `non terminal int expr;` 中，`RESULT` 就是 `int`。

动作也可以写在产生式中间：

```cup
stmt ::= BEGIN {: enterScope(); :} stmt_list END {: leaveScope(); :};
```

中间动作只能访问它左边已经出现的标签。

## 优先级和结合性

表达式文法通常天然有二义性。可以用优先级声明解决：

```cup
precedence left PLUS, MINUS;
precedence left TIMES, DIVIDE;
precedence right UMINUS;
```

越靠后的声明优先级越高。`left` 表示左结合，`right` 表示右结合，`nonassoc` 表示不可结合。

一元负号这类场景可以用 `%prec` 指定当前产生式的优先级：

```cup
expr ::= MINUS expr:e {: RESULT = -e; :} %prec UMINUS;
```

## EBNF 简写

本项目支持常用 EBNF 简写，能减少样板产生式。

### 可选项

```cup
param ::= modifier?:modifier type:t IDENT:name;
```

`X?` 表示 `X` 可有可无。没有匹配到时，对应值为 `null`。

### 重复列表

```cup
program ::= decl*:declList stmt*:stmtList;
args    ::= expr[COMMA !]*:args;
ids     ::= IDENT[COMMA !]+:idList;
```

| 写法          | 含义                       |
|-------------|--------------------------|
| `X*`        | 零个或多个 `X`                |
| `X+`        | 一个或多个 `X`                |
| `X[SEP !]*` | 用 `SEP` 分隔，允许为空，不允许尾随分隔符 |
| `X[SEP !]+` | 用 `SEP` 分隔，至少一个，不允许尾随分隔符 |
| `X[SEP ?]*` | 用 `SEP` 分隔，允许为空，允许尾随分隔符  |
| `X[SEP ?]+` | 用 `SEP` 分隔，至少一个，允许尾随分隔符  |

列表值类型为 `List<T>`。例如 `terminal int NUMBER;` 中，`NUMBER*` 的值类型是 `List<Integer>`。

### 匿名表达式和匿名符号

可以用括号直接在产生式里写一个局部表达式：

```cup
stmt ::= IF LPAR cond:cond RPAR (stmt | BEGIN stmt*:body END);
```

这种括号表达式称为匿名表达式。CUP 会把它提升成一个匿名符号，因此它可以像普通符号一样加标签，也可以在 AST 模式下成为一个子节点。

匿名表达式里可以包含多个分支：

```cup
primary ::= (IDENT:name | INTCONST:value | LPAR expr:inner RPAR):value;
```

如果多个地方出现结构相同的匿名表达式，CUP 会复用同一个匿名符号，而不是为每个位置生成一份独立语法，标签和 AST 字段仍按各自出现的位置使用。

不要依赖匿名符号的生成名称，如果需要一个稳定且有意义的名称，建议提取成具名非终结符。

匿名表达式内部的标签只在这个匿名表达式自己的动作或生成的匿名 AST 节点中可见。父产生式不会直接得到内部标签变量；如果父产生式需要访问整个匿名表达式的结果，要给匿名表达式本身加标签：

```cup
stmt ::= IF LPAR cond:cond RPAR (stmt:single | BEGIN stmt*:body END):thenPart;
```

在这个例子中，父产生式可以访问 `thenPart`，但不能直接访问 `single` 或 `body`。如果启用了 AST，`thenPart` 对应的匿名节点上会保留 `single` / `body` 这类内部字段；如果希望父节点直接拥有这些字段，应使用 AST 模式下的字段展开 `...`。

匿名表达式的每个分支都可以像普通产生式一样在末尾写动作，并通过 `RESULT` 设置这个分支的返回值：

```cup
expr ::= (::int
           NUMBER:n             {: RESULT = n; :}
         | LPAR expr:e RPAR     {: RESULT = e; :}
         ):value;
```

`::int` 是匿名表达式的返回类型声明。父产生式给匿名表达式加标签后，就可以按这个类型读取它的值，例如上面的 `value` 是 `int`。

如果没有写返回类型，非 AST 模式下匿名表达式的标签值按 `Object` 处理。如果某个分支没有写会设置 `RESULT` 的动作，这个分支默认不会产生业务值。

建议只把匿名表达式用于短小、局部的语法，为了避免产生过于复杂的嵌套以及降低生成器的复杂度，CUP 禁止匿名表达式嵌套。

## 自动 AST

启用自动 AST：

```bash
java -jar java-cup.jar -ast Node%s -parser MiniJavaParser -symbols MiniJavaSymbols -nonterms -destdir generated minijava.cup
```

AST 模式下，通常把需要生成节点的非终结符声明为 `AstNode`：

```cup
import java_cup.runtime.*;

terminal IDENT, INTCONST, ASSIGN, SEMI;
non terminal AstNode stmt, expr;

stmt ::= IDENT:lhs ASSIGN expr:rhs SEMI %namer Assign;

expr ::= IDENT:name
       | INTCONST:value
       ;
```

生成后的节点支持：

| 能力       | 用法                         |
|----------|----------------------------|
| 读取节点名    | `node.getNodeName()`       |
| 读取源码位置   | `node.getLocation()`       |
| 判断字段是否存在 | `node.hasLabel("rhs")`     |
| 按标签读取子节点 | `node.getByLabel("rhs")`   |
| 遍历子节点    | `for (var child : node)`   |
| 打印树      | `node.toTreeString(false)` |

带标签的字段还会生成更方便的类型化方法，例如 `getRhs()`、`hasElseStmt()`。

匿名表达式在 AST 模式下也会生成对应的匿名 AST 节点。如果匿名表达式没有手写动作，CUP 会根据其中的标签自动构造这个节点；一旦写了手动动作，就由动作里的 `RESULT` 决定返回值。

### 字段展开

字段展开只用于 AST 模式。启用 `-ast` 后，如果一个子节点只是为了组织语法，而你希望父节点直接拥有它的字段，可以在符号前加 `...`：

```cup
stmt ::= ...simpleStmt SEMI;
```

如果 `simpleStmt` 中有 `lhs`、`rhs` 等字段，展开后这些字段会直接出现在 `stmt` 生成的节点上，而不是先通过 `simpleStmt` 子节点再访问。

`...` 也可以用于匿名表达式：

```cup
stmt ::= ...(IDENT:lhs ASSIGN expr:rhs) SEMI %namer AssignStmt;
```

展开适合“语法上需要分层、语义上不想多一层节点”的场景。非 AST 模式不要使用 `...`；不要对列表使用 `...`，例如 `...expr*` 是无效的；动作代码也不能被展开。

### 产生式命名

`%namer` 可以给 AST 节点变体一个稳定、可读的名字：

```cup
stmt ::= IDENT:lhs ASSIGN expr:rhs SEMI %namer Assign;
```

建议在重要语法节点上使用 `%namer`，这样生成代码和调试输出更容易理解。

### AST 使用示例

```java
ComplexSymbolFactory sf = new ComplexSymbolFactory(
    MiniJavaSymbols.TERMINAL_NAMES,
    MiniJavaSymbols.NON_TERMINAL_NAMES
);

MiniJavaParser parser = new MiniJavaParser(new Lexer(reader, sf), sf);
AstNode root = parser.parse().value();

System.out.println(root.toTreeString(false));
```

## 扫描器集成

扫描器负责把输入字符转换为 CUP `Symbol`。它需要实现：

```java
public interface Scanner {
    Symbol next_token() throws Exception;
}
```

推荐用 `ComplexSymbolFactory` 创建 token：

```java
ComplexSymbolFactory sf = new ComplexSymbolFactory(
    CalcSymbols.TERMINAL_NAMES,
    CalcSymbols.NON_TERMINAL_NAMES
);

return sf.newSymbol(
    CalcSymbols.NUMBER,
    ComplexLocation.ofInclusive(line, startColumn, line, endColumn),
    value
);
```

无值 token：

```java
return sf.newSymbol(CalcSymbols.PLUS, ComplexLocation.ofInclusive(line, column, line, column));
```

到达输入结束时返回 EOF：

```java
return sf.newSymbol(CalcSymbols.EOF, ComplexLocation.NO_LOCATION);
```

## JFlex 示例

JFlex 与 CUP 配合时，通常使用 `%cup`：

```jflex
%cup
%line
%column

%%

[0-9]+ {
  int value = Integer.parseInt(yytext());
  return symbolFactory.newSymbol(
    CalcSymbols.NUMBER,
    ComplexLocation.ofInclusive(yyline + 1, yycolumn + 1, yyline + 1, yycolumn + yylength()),
    value
  );
}

"+" {
  return symbolFactory.newSymbol(
    CalcSymbols.PLUS,
    ComplexLocation.ofInclusive(yyline + 1, yycolumn + 1, yyline + 1, yycolumn + 1)
  );
}
```

注意：`ComplexLocation` 使用 1-based 行列号。`ofInclusive` 的结束位置是包含式，适合 JFlex 这类按 token 长度计算位置的场景。

## 运行解析器

普通解析：

```java
CalcParser parser = new CalcParser(scanner, symbolFactory);
Symbol result = parser.parse();
```

读取结果：

```java
int value = result.getAsInt();
```

如果起始非终结符是对象类型，例如 `AstNode` 或 `String`，需要使用 `value()` 读取。

调试解析：

```java
parser.debug_parse();
```

如果需要自定义错误信息，可以在 `.cup` 中覆盖 parser 方法：

```cup
parser code {:
  @Override
  public void report_error(String message, Object info) {
    System.err.println(message);
  }
:};
```

## 错误恢复

CUP 提供特殊 token `error` 用于错误恢复。常见写法是在语句级别跳过到分号：

```cup
stmt ::= error SEMI {: report_error("跳过错误语句", null); :}
       | expr SEMI
       ;
```

建议把错误恢复写在较稳定的边界上，例如语句结束符、块结束符或逗号分隔项。不要在过小的表达式局部滥用 `error`，否则错误信息可能变得难以理解。

## 常用命令行选项

| 选项              | 说明                      |
|-----------------|-------------------------|
| `-parser name`  | 指定生成的解析器类名              |
| `-symbols name` | 指定符号常量类名                |
| `-interface`    | 把符号常量生成接口               |
| `-destdir dir`  | 指定输出目录                  |
| `-package name` | 指定生成类包名                 |
| `-nonterms`     | 在符号类中输出非终结符常量           |
| `-ast [format]` | 启用自动 AST，默认格式为 `Node%s` |
| `-expect n`     | 声明预期冲突数                 |
| `-nowarn`       | 不显示警告                   |
| `-nosummary`    | 不显示生成摘要                 |

诊断选项：

| 选项              | 说明            |
|-----------------|---------------|
| `-progress`     | 显示生成进度        |
| `-time`         | 显示耗时统计        |
| `-dump_grammar` | 输出文法信息        |
| `-dump_states`  | 输出状态机信息       |
| `-dump_tables`  | 输出分析表信息       |
| `-dump`         | 同时启用以上 dump   |
| `-debug`        | 调试 CUP 规范解析过程 |

高级选项：

| 选项                | 说明                     |
|-------------------|------------------------|
| `-typearg args`   | 给生成 parser 添加类型参数      |
| `-symbol class`   | 使用自定义 `Symbol` 实现      |
| `-location class` | 使用自定义位置类型              |
| `-compact_red`    | 压缩 reduce 表            |
| `-noscanner`      | 不生成默认 Scanner 构造器      |
| `-auto_clear`     | 生成前清理输出目录中的 `.java` 文件 |

## 常见问题

| 问题                | 处理方式                                 |
|-------------------|--------------------------------------|
| 提示符号未声明           | 检查 `terminal` / `non terminal` 声明和拼写 |
| shift/reduce 冲突过多 | 检查优先级、结合性和 `%prec`；确实可接受时用 `-expect` |
| 动作中找不到标签变量        | 确认标签写在符号后，例如 `expr:e`                |
| `RESULT` 类型不对     | 检查左侧非终结符声明的类型                        |
| AST 中缺字段          | 确认产生式右侧符号有标签                         |
| AST 产生式名称难读       | 给产生式加 `%namer`                       |
| 位置信息不准确           | 确认扫描器传入的是正确的 1-based 行列号             |
| 生成代码编译失败          | 确认 classpath 包含 CUP 运行时 jar 和生成目录    |

## 建议实践

- 表达式文法优先使用 `precedence` 和 `%prec` 解决优先级，而不是拆出大量层级。
- 需要后续分析或转换时，优先启用 `-ast` 并给重要产生式加 `%namer`。
- 扫描器统一通过 `SymbolFactory` 创建 token，避免手写不同类型的 `Symbol`。
- 标签命名使用业务含义，例如 `lhs`、`rhs`、`condition`，不要只用 `a`、`b`。
- 错误恢复放在语句、声明、列表项等清晰边界上。
- 生成后的代码仍应在目标项目中编译、运行和测试；文档示例不能替代项目级验证。