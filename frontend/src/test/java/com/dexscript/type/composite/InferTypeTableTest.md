# interface_type_table

```dexscript
interface MyInf {
    <T1>: string
    <T2>: int64
}
```

| path                   | name | type     |
| ---------------------- | ---- | -------- |
| `topLevelDecls[0].inf` | T1   | `string` |
| `topLevelDecls[0].inf` | T2   | `int64`  |

# interface_function_type_table

```dexscript
interface MyInf {
    ::MyFunc(<T>:string)
}
```

| path                   | name | type     |
| ---------------------- | ---- | -------- |
| `topLevelDecls[0].inf.functions[0]` | T   | `string` |

