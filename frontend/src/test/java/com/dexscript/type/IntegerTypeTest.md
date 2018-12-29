# assignable_relationship

| assignable | to           | from         |
| ---------- | ------------ | ------------ |
| true       | `int64`      | `int64`      |
| true       | `int64`      | `100`        |
| true       | `int64`      | `(const)100` |
| false      | `int64`      | `int32`      |
| true       | `int32`      | `int32`      |
| true       | `int32`      | `100`        |
| true       | `int32`      | `(const)100` |
| false      | `int32`      | `int64`      |
| true       | `100`        | `100`        |
| false      | `101`        | `100`        |
| true       | `100`        | `(const)100` |
| false      | `101`        | `(const)100` |
| false      | `100`        | `int32`      |
| false      | `100`        | `int64`      |
| false      | `(const)100` | `(const)100` |
| false      | `(const)100` | `100`        |
| false      | `(const)100` | `int32`      |
| false      | `(const)100` | `int64`      |

# equals_relationship

| assignable | to           | from         |
| ---------- | ------------ | ------------ |
| true       | `int64`      | `int64`      |
| false      | `int64`      | `int32`      |
| false      | `int64`      | `100`        |
| false      | `int64`      | `(const)100` |
| true       | `int32`      | `int32`      |
| true       | `100`        | `100`        |
| false      | `101`        | `100`        |
| false      | `100`        | `(const)100` |
| true       | `(const)100` | `(const)100` |
| false      | `(const)101` | `(const)100` |
