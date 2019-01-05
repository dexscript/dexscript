# assignable_relationship

| assignable | to            | from           |
| ---------- | ------------- | -------------- |
| true       | `bool`        | `bool`         |
| false      | `bool`        | `string`       |
| true       | `bool`        | `false`        |
| true       | `bool`        | `true`         |
| true       | `bool`        | `(const)false` |
| true       | `bool`        | `(const)true`  |
| false      | `true`        | `bool`         |
| true       | `true`        | `true`         |
| false      | `true`        | `false`        |
| true       | `true`        | `(const)true`  |
| false      | `true`        | `(const)false` |
| false      | `(const)true` | `bool`         |
| false      | `(const)true` | `true`         |
| false      | `(const)true` | `(const)true`  |

# equals_relationship

| equals | left          | right          |
| ------ | ------------- | -------------- |
| true   | `bool`        | `bool`         |
| false  | `bool`        | `string`       |
| true   | `true`        | `true`         |
| false  | `true`        | `false`        |
| true   | `(const)true` | `(const)true`  |
| false  | `(const)true` | `(const)false` |

