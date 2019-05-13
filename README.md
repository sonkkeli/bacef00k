# bacef00k    
https://web-palvelinohjelmointi-19.mooc.fi/    
Available on heroku: https://bacef00k.herokuapp.com/    
(for testing you can log in with username=username and password=password)    
Atm everything else works but liking can be done twice... And you can bypass some of the access controls via postman... On the to-do-list, but not prioritized unfortunately...

Kurssiin Web-palvelinohjelmointi Java kuuluu ohjelmointiprojekti, jossa luodaan kaveri- ja kuvasovellus eli tuttavallisemmin vanhan kansan Facebook. 

Sovellukselta odotetut ominaisuudet:

### Käyttäjien rekisteröityminen

Käyttäjä rekisteröityy sovellukseen kirjaamalla sovellukseen käyttäjätunnuksen, salasanan sekä nimen. Tämän lisäksi käyttäjältä kysytään profiilin näyttämisessä käytettävää merkkijonoa, jonka perusteella käyttäjän sivu voidaan löytää sovelluksesta. Esim. “https://sovellus.net/kayttajat/profiili-merkkijono”

### Käyttäjien etsiminen ja kaveripyynnön tekeminen

Käyttäjä voi etsiä muita käyttäjiä nimen perusteella. Käyttäjä voi lähettää kaveripyyntöjä muille järjestelmässä oleville käyttäjille. Käyttäjä myös tietää kaverinsa.

### Kaveripyynnön hyväksyntä

Käyttäjä voi tarkastella saamiaan kaveripyyntöjä. Kaveripyynnön yhteydessä näytetään kaveripyynnön tekijän nimi sekä kaveripyynnön tekoaika. Mikäli kaveripyyntö hyväksytään, kaveripyynnön tekijä lisätään käyttäjän kaveriksi. Kaveripyynnön voi myös halutessaan hylätä.

### Kuva-albumi

Jokaisella käyttäjällä on kuva-albumi. Käyttäjä voi lisätä albumiinsa kuvia ja myös poistaa niitä. Kunkin käyttäjän kuva-albumi voi sisältää korkeintaan 10 kuvaa. Jokaiseen kuvaan liittyy myös tekstimuotoinen kuvaus, joka lisätään kuvaan kuvan lisäyksen yhteydessä.

### Profiilikuva

Käyttäjä voi määritellä yhden kuva-albumissa olevan kuvan profiilikuvaksi.

### Henkilökohtainen seinä

Jokaisella käyttäjällä on henkilökohtainen “seinä”, joka sisältää henkilön nimen sekä mahdollisesti määritellyn profiilikuvan. Sekä käyttäjä että käyttäjän kaverit voivat lähettää seinälle tekstimuotoisia viestejä. Jokaisesta viestistä näytetään viestin lähettäjän nimi, viestin lähetysaika, sekä viestin tekstimuotoinen sisältö. Viestit näytetään seinällä niiden saapumisjärjestyksessä siten, että seinällä näkyy aina korkeintaan 25 uusinta viestiä.

### Tykkääminen

Käyttäjän kaverit voivat tykätä kuvista ja seinällä olevista viesteistä. Tykkääminen tapahtuu viestin ja kuvan yhteydessä olevaa tykkäysnappia painamalla. Kukin käyttäjä voi tykätä tietystä kuvasta ja tietystä viestistä korkeintaan kerran (sama käyttäjä ei saa lisätä useampaa tykkäystä tiettyyn kuvaan tai viestiin). Viestien ja kuvien näytön yhteydessä näytetään niihin liittyvä tykkäysten lukumäärä.

### Kommentointi

Samalla tavalla kuin tykkääminen, kaverit voivat myös kommentoida kuvia ja viestejä. Kommentointi tapahtuu viestin ja kuvan yhteydessä olevan kommentointikentän avulla. Kuvien ja viestien yhteydessä näytetään aina korkeintaan 10 uusinta kommenttia.
