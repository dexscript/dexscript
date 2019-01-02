# use_array_list

```dexscript
interface :: {
    New__(<T>: interface{}, class: 'ArrayList'): List<T>
}
interface List {
    <T>: interface{}
    add(e: T): bool
    get(index: int32): T
}
```

```dexscript
function Hello(): int64 {
    list := new ArrayList<int64>()
    list.add(100)
    return list.get(0)
}
```

