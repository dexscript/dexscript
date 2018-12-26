# matched

```dexscript
var msg: string
```

* identifier
    * "msg"
* type
    * "string"

# no_space_between_var_and_identifier

```dexscript
varmsg: string
```

```dexscript
<unmatched>varmsg: string</unmatched>
```

# missing_identifier_recover_by_colon

```dexscript
var ?:string
```

```dexscript
var <error/>?:string
```

# missing_identifier_recover_by_blank

```dexscript
var ? :string
```

```dexscript
var <error/>? :string
```

# missing_identifier_recover_by_line_end

```dexscript
var ?;:string
```

```dexscript
var <error/>?
```

# missing_colon_recover_by_blank

```dexscript
var msg( string
```

```dexscript
var msg<error/>( string
```

# missing_colon_recover_by_line_end

```dexscript
var msg(; string
```

```dexscript
var msg<error/>(
```

# missing_type

```dexscript
var msg: ??
```

```dexscript
var msg:<error/> ??
```
