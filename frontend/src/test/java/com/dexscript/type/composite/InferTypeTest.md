# interface_method_param

```dexscript
interface MyInf {
    <T1>: string
    MyMethod(<T2>: int64, arg0: T1, arg1: T2)
}
```


| path                   | type     |
| ---------------------- | -------- |
| `topLevelDecls[0].inf.methods[0].sig.params[0].paramType` | `string` |
| `topLevelDecls[0].inf.methods[0].sig.params[1].paramType` | `int64` |

# actor_local_variable

```dexscript
function MyFunc(<T>: string) {
    var MyVar: T
}
```


| path                   | type     |
| ---------------------- | -------- |
| `topLevelDecls[0].actor.stmts[0].type` | `string` |
