# empty

```dexscript
{}
```

* stmts
    * size
        * 0

# one_statement

```dexscript
{hello()}
```

* stmts
    * size
        * 1
    * [0]
        * "hello()"

# statements_separated_by_new_line

```dexscript
{hello()
world()}
```

* stmts
    * size
        * 2
    * [0]
        * "hello()"
    * [1]
        * "world()"

# statements_separated_by_semicolon

```dexscript
{hello();world()}
```

* stmts
    * size
        * 2
    * [0]
        * "hello()"
    * [1]
        * "world()"

# walk_up

```dexscript
{return example; return def}
```

* stmts
    * [0]
        * prev
            * `{return example; return def}`
    * [1]
        * prev
            * `return example`

# recover_from_invalid_statement_by_line_end

```dexscript
{
??
return example
}
```

```dexscript
{
<error/>??
return example
}
```

# recover_from_last_invalid_statement

```dexscript
{
return example;??}xyz
```

```dexscript
{
return example;<error/>??}
```



