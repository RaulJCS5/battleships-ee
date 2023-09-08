# PDM LEIC51D 2223i - Aula 12

O que é um SharedPreferences é um contentor de par chave valor, a.k.a key-value container, que é armazenado de forma persistente, portanto é armazenado no file system e que sobrevive ás execuções do processo
SharedPreferences
edit()
apply()
commit()

# PDM LEIC51D 2223i - Aula 13

LobbyActivity
Atualização da UI
Recomposição
Tem que estar dentro de um composable porque senão não acontece não fica associado a nenhuma recomposição
ViewModel é destruído ViewModelScope é cancelado
Cancelamento está implícito na destruição do ViewModel
Eu quero cancelar enquanto a activity não está visível como é que eu faço?
Com o uso do Job.
Quero entrar quando a activity/lobby sair quando activity deixar de estar visível
Como é que isso se faz?
Intercetando inventos de lifecycle
