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

# interface_method_type_table

```dexscript
interface MyInf {
    MyMethod(<T>: string)
}
```

| path                   | name | type     |
| ---------------------- | ---- | -------- |
| `topLevelDecls[0].inf.methods[0]` | T   | `string` |


# actor_type_table

```dexscript
function MyFunc(<T>: string) {
}
```

| path                   | name | type     |
| ---------------------- | ---- | -------- |
| `topLevelDecls[0].actor` | T   | `string` |

# inner_actor_type_table

```dexscript
function MyFunc() {
    await {
    case MyInnerFunc(<T>: string) {
    }}
}
```

| path                   | name | type     |
| ---------------------- | ---- | -------- |
| `topLevelDecls[0].actor.stmts[0].cases[0]` | T   | `string` |

# sig_type_table

```dexscript
function MyFunc(<T>: string) {
}
```

| path                   | name | type     |
| ---------------------- | ---- | -------- |
| `topLevelDecls[0].actor.sig` | T   | `string` |