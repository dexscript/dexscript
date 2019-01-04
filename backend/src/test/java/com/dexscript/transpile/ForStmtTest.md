# for_0_to_102400_without_await

```dexscript
function Hello(): int64 {
    var total: int64
    for i := 0; i < 102400; i++ {
        total = total + i
    }
    return total
}
```

* "5242828800"

# for_0_to_102400_with_await

```dexscript
function Hello(): int64 {
    var total: int64
    for i := 0; i < 102400; i++ {
        asIs := new AsIs(i)
        total = total + <-asIs
    }
    return total
}
function AsIs(i: int64): int64 {
    return i
}
```

* "5242828800"

# for_break_without_await

```dexscript
function Hello(): int64 {
    i := 0
    total := 0
    for {
        i++
        total = total + i
        if (i > 10) {
            break
        }
    }
    return total
}
```

* 66

# for_break_with_await

```dexscript

function Hello(): int64 {
    i := 0
    total := 0
    for {
        i++
        val := new Add(total, i)
        total = <-val
        if (i > 10) {
            break
        }
    }
    return total
}
function Add(left: int64, right: int64): int64 {
    return left + right
}
```

* 66

# for_return_without_await

```dexscript
function Hello(): int64 {
    i := 0
    total := 0
    for {
        i++
        total = total + i
        if (i > 10) {
            return total
        }
    }
}
```

* 66

# for_return_with_await

```dexscript

function Hello(): int64 {
    i := 0
    total := 0
    for {
        i++
        val := new Add(total, i)
        total = <-val
        if (i > 10) {
            return total
        }
    }
}
function Add(left: int64, right: int64): int64 {
    return left + right
}
```

* 66

