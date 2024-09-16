# Chat Examples

All Chat examples are packaged in `chat` module, e.g.
* Chat Example 01
* Chat Example 02
* Chat Example 03
* Chat Example 04
* Chat Example 05
* Chat Example 06

## Chat Example 01

Open the `BasicPromptController.java` and explore the contents.

Test The Endpoint:

<table>
<tr>
<th>Command</th>
<th>URL</th>
</tr>
<tr>
<td>

```
http localhost:8080/chat/01/joke
```

</td>
<td>

```
http://localhost:8080/chat/01/joke
```

</td>
</tr>
</table>


## Chat Example 02

Open the `PromptController.java` and explore the contents.

Test The Endpoint:
<table>
<tr>
<th>Command</th>
<th>URL</th>
</tr>
<tr>
<td>

```
http localhost:8080/chat/02/joke
```
```
http localhost:8080/chat/02/threeJokes
```
</td>
<td>

```
http://localhost:8080/chat/02/joke
```
```
http://localhost:8080/chat/02/threeJokes
```
</td>
</tr>
</table>

## Chat Example 03

Open the `PromptTemplateController.java` and explore the contents.

Test The Endpoint:
<table>
<tr>
<th>Command</th>
<th>URL</th>
</tr>
<tr>
<td>

```
http localhost:8080/chat/03/joke
```
```
http localhost:8080/chat/03/plays
```
</td>
<td>

```
http://localhost:8080/chat/03/joke
```
```
http://localhost:8080/chat/03/plays
```
</td>
</tr>
</table>


## Chat Example 04

Open the `StructuredOutputConverterController.java` and explore the contents.

Test The Endpoint:
<table>
<tr>
<th>Command</th>
<th>URL</th>
</tr>
<tr>
<td>

```
http localhost:8080/chat/04/plays
```
```
http localhost:8080/chat/04/plays/list
```
```
http localhost:8080/chat/04/plays/map
```
```
http localhost:8080/chat/04/plays/object
```
</td>
<td>

```
http://localhost:8080/chat/04/plays
```
```
http://localhost:8080/chat/04/plays/list
```
```
http://localhost:8080/chat/04/plays/map
```
```
http://localhost:8080/chat/04/plays/object
```
</td>
</tr>
</table>


## Chat Example 05

Open the `FunctionController.java` and explore the contents.

Test The Endpoint:
<table>
<tr>
<th>Command</th>
<th>URL</th>
</tr>
<tr>
<td>

```
http localhost:8080/chat/05/pack
```
```
http localhost:8080/chat/05/weather
```
</td>
<td>

```
http://localhost:8080/chat/05/pack
```
```
http://localhost:8080/chat/05/weather
```
</td>
</tr>
</table>


## Chat Example 06

Open the `RoleController.java` and explore the contents.

Test The Endpoint:
<table>
<tr>
<th>Command</th>
<th>URL</th>
</tr>
<tr>
<td>

```
http localhost:8080/chat/06/fruit
```
```
http localhost:8080/chat/06/veg
```
</td>
<td>

```
http://localhost:8080/chat/06/fruit
```
```
http://localhost:8080/chat/06/veg
```
</td>
</tr>
</table>
