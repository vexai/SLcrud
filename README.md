# SLcrud

System bankowości online.

W związku z tym, że mam error z heroku z połączeniem z bazą danych to aby odpalić projekt lokalnie wystarczy
założyć bazę danych "login" w mysql i dodać hasło jakie mamy ustawione w bazie do application.properties.
Aplikacja odpala się na porcie 8080.

Admin jest predefiniowany w kodzie w data.sql i może być jeden, ma możliwość usunięcia użytkowników 
z rolą "ROLE_USER", login: admin, hasło: 111111.

Każdemu potencjalnemu użytkownikowi pozwala stworzyć swoje unikatowe konto i się do niego zalogować. 
Wejścia przez endpointy bez zalogowania są chronione w security, więc użytkownik nie może wejść na 
konto kogoś innego bez poznania loginu i hasła.
Aplikacja umożliwia wpłacenie i wypłacenie środków na konto. Dodatkowo środki można przesyłać między użytkownikami.
Jest też możliwość wysyłania wiadomości między użytkownikami.

Wszystkie operacje wpłacenia/wypłacenia/transferu są zapisywane i użytkownik może je zobaczyć u siebie w panelu.
Taka sama sytuacja z wiadomościami wysłane/odebrane.
