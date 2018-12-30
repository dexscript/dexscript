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
| Hello    | `'a'`         | 1               | `Hello(arg0: 'a'): void` | undefined                   | undefined                        |
| Hello    | `string`      | 2               | `Hello(arg0: 'a'): void` | `Hello(arg0: string): void` | undefined                        |
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

# impl_should_override_interface

```dexscript
interface HelloInf {
    SayHello(msg: string)
}
```

```dexscript
SayHello(self: int64, msg: interface{})
```

| funcName | posArgs           | candidates.size |
| -------- | ----------------- | --------------- |
| SayHello | `HelloInf,string` | 0               |


# widen_const_type

```dexscript
Hello(arg0: int32)
```

| funcName | posArgs      | candidates.size | `args[0]` |
| -------- | ------------ | --------------- | --------- |
| Hello    | `(const)100` | 1               | `int32`   |

# call_with_named_arg

```dexscript
Hello(a: int32)
```

only named args, no positional args

| funcName | posArgs | namedArgs | candidates.size | `namedArgsMapping[0]` |
| -------- | ------- | --------- | --------------- | --------------------- |
| Hello    |         | `a=int32` | 1               | 0                     |
| Hello    |         | `b=int32` | 0               | undefined             |

# call_with_two_named_args

```dexscript
Hello(a: int32, b: int64)
```

| funcName | posArgs | namedArgs          | candidates.size | `namedArgsMapping[0]` | `namedArgsMapping[1]` |
| -------- | ------- | ------------------ | --------------- | --------------------- | --------------------- |
| Hello    |         | `b=int64, a=int32` | 1               | 1                     | 0                     |
| Hello    |         | `a=int32`          | 0               | undefined             | undefined             |