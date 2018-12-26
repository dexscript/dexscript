# matched

```dexscript
 return example
```

```dexscript
return example
```

* expr
    * "example"

# garbage_in_prelude

```dexscript
ddreturn example
```

```dexscript
<unmatched>ddreturn example</unmatched>
```

# missing_expr_recover_by_file_end

```dexscript
return

```

```dexscript
return
<error/>
```

# missing_expr_recover_by_line_end

```dexscript
return ; example
```

```dexscript
return <error/>
```

# return_without_space

```dexscript
returnabc
```

```dexscript
<unmatched>returnabc</unmatched>
```

# walk_up

```dexscript
return example
```

* expr
    * prev
        * "return example"





