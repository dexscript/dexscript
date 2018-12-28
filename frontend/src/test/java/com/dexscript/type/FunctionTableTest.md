# only_match_one

```dexscript
Hello(arg0: string)
```

```dexscript
Hello(arg0: int64)
```

| funcName | posArgs  | candidates.size | `candidates[0].func`        |
| -------- | -------- | --------------- | --------------------------- |
| Hello    | `string` | 1               | `Hello(arg0: string): void` |
| Hello    | `int64`  | 1               | `Hello(arg0: int64): void`  |

# need_runtime_check

```dexscript
Hello(arg0: 'a')
```

```dexscript
Hello(arg0: string)
```

```dexscript
Hello(arg0: interface{})
```

| funcName | posArgs       | candidates.size | `candidates[0].func`     | `candidates[1].func`        | `candidates[2].func`             |
| -------- | ------------- | --------------- | ------------------------ | --------------------------- | -------------------------------- |
| Hello    | `'a'`         | 1               | `Hello(arg0: 'a'): void` | null                        | null                             |
| Hello    | `string`      | 2               | `Hello(arg0: 'a'): void` | `Hello(arg0: string): void` | null                             |
| Hello    | `interface{}` | 3               | `Hello(arg0: 'a'): void` | `Hello(arg0: string): void` | `Hello(arg0: interface{}): void` |

# if_matched_then_following_candidates_will_be_skipped

```dexscript
Hello(arg0: string)
```

```dexscript
Hello(arg0: 'a')
```

| funcName | posArgs | candidates.size | skippeds.size | `candidates[0].func`        |
| -------- | ------- | --------------- | ------------- | --------------------------- |
| Hello    | `'a'`   | 1               | 1             | `Hello(arg0: string): void` |

# if_not_match_then_candidate_will_be_ignored

```dexscript
Hello(arg0: 'a')
```

| funcName | posArgs  | candidates.size | ignoreds.size |
| -------- | -------- | --------------- | ------------- |
| Hello    | `string` | 0               | 1             |

# invoke_interface_without_impl

```dexscript
interface HelloInf {
    SayHello(msg: string)
}
```

| funcName | posArgs           | candidates.size | ignoreds.size |
| -------- | ----------------- | --------------- | ------------- |
| SayHello | `HelloInf,string` | 0               | 1             |

# invoke_interface_with_impl

```dexscript
interface HelloInf {
    SayHello(msg: string)
}
```

```dexscript
SayHello(self: int64, msg: string)
```

| funcName | posArgs           | candidates.size |
| -------- | ----------------- | --------------- |
| SayHello | `HelloInf,string` | 1               |
