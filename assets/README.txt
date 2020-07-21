Questo file descrive come sono stati creati i file che vengono utilizzati in lettura dal codice
sia per la versione RisikoClassic che per la versione SPQRisiko

File "territori.txt"
Numero di territori
CodTerr	NumFigura ColoreHEX posX posY NumConfini (in base al numero di confini ci saranno tot codici dopo) NomeTerr Continente

Corrispondenze NumFigura:
1 --> Jolly
2 --> Fante
3 --> Cannone
4 --> Cavaliere

File "continenti.txt"
Numero di continenti
CodCont NomeCont Bonus

File "obiettivi.txt"
TipoOb CodOb NumConquiste/CodCont MinTanks/CodCont ConqTerzoCont (0=no, 1=si)