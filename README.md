<h1 align="center">
  <a href="https://github.com/consiglionazionaledellericerche/jada">
    JADA JEE Framework
  </a>
</h1>

Framework software per applicazioni basate su EJB3. Permette allo sviluppatore di configurare aspetti relativi alla presentazione ed alla persistenza dei dati, 
quali la composizione delle maschere di visualizzazione/modifica e le operazioni di "CRUD" (create,read,update,delete). Tale configurazione può essere effettuata all'interno del codice sorgente, tramite "annotations" non standard definite dal framework, o atraverso file XML associati all'aplicazione. Il framework consente inoltre la paginazione lato server di liste di elementi, che non potrebbero essere convenientemente visualizzati su un'unica pagina, ed il supporto a transazioni di durata molto elevata, necessarie per attività tipiche di operatori che effettuano inserimenti dati caratterizzate da tempi molto lunghi.

Il framework JADA è utilizzato come base di sviluppo da molte applicazioni strategicamente importanti all'interno dell'ente, quali: SIGLA.

**Registrato alla SIAE Numero brevetto 17-11-2009/007411**

## 📖 Documentazione

**ActionServlet**

Servlet per la gestione di action.
ActionServlet smista le richieste in seguito ad una SUBMIT di una form HTML ad una istanza di Action in base alla configurazione contenuta in un file XML.
Ad ogni richiesta la servlet controlla se il file di configurazione è stato aggiornato e in tal caso lo rilegge.

La vita di una action segue il seguente schema: La richiesta HTML viene convertita in base alla mappatura
nella classe che la implementa. Se la action necessita di una HttpSession già attiva e se questa condizione
non è verificata viene cercato nell'ambito della action un forward dal nome sessionExpired e viene eseguito.

All'interno dei parametri della richiesta viene cercato quello relativo al nome del business process in cui
deve essere contestualizzata la action e viene passato all'ActionContext.
La action viene istanziata e ogni property a cui corrisponde un parametro della richiesta viene settata
col valore corrispondente. La action viene eseguita.
Se il forward restituito dalla action è nullo viene cercato un forward dal nome default e viene eseguito.
altrimenti viene eseguito il forward restituito dalla action.

Se in una qualsiasi fase si verifica una eccezione non gestita la servlet cerca il forward dal nome
uncaughtException e lo esegue.
Il file di configurazione definisce tre elementi:
Action: definisce una action e la classe che la implementa.
è possibile impostare uno o più parametri di inizializzazione della action che vengono
passati all'istanza che implementa la action prima della sua effettiva esecuzione tramite init().
è possibile richiedere che una action debba avere per forza una una session già viva.
BusinessProcess: definisce un business process e la classe che lo implementa.
Un business process viene mantenuto nella session della servlet ed è possibile
associare ogni form html con un particolare business process.
è possibile impostare uno o più parametri di inizializzazione del bp che vengono
passati all'istanza che lo implementa tramite init()
Forward: definisce il nome di un forward statico e l'url a cui è associato.

I forward possono essere definiti a tre livelli:
business process: hanno la precedenza su tutti gli altri.
action servlet: se non viene trovato agli altri livelli.

Esempio di un file di configurazione:
nome del forward path=url /> nome della action actionClass=classe che implementa la action
needExistingSession=true|false > nome del parametro value=valore />
nome del forward path=url />
nome del bp className=Classe che lo implementa >
nome del parametro value=valore />
nome del forward path=url />

Per un corretto funzionamento la servlet deve essere dichiarata nel file di configurazione
della web application e associata ad una servlet-path del tipo "*.[estensione]".
In tal modo ogni url terminante nell'estesione specificata viene interpretato dalla servlet.
L'estesione di defaul è ".do".

Per default la servlet legge la configurazione dal file denominato actions.xml che si trova
nella directory root della web application a cui appartiene la servlet.
è possibile tuttavia utilizzare un altro file di configurazione passadone il nome alla servlet come
parametro di inizializzazione dal nome actions

**BusinessProcess**

Un BusinessProcess ha la responsabilità di mantenere lo stato del processo di business che è stato
avviato in seguito alla richiesta di un client.

I BusinessProcess vengono mantenuti nella sessione, quindi sopravvivono tra una action e l'altra.
Ogni richiesta originata da un client determina la selezione di un business process corrente.
Durante la valutazione della Action o nell'esecuzione di una JSP in seguito alla valutazione di un
forward viene mantenuto il riferimento a tale business process, per cui   possibile colloquiare
con tale istanza per reperire tutte le informazioni necessarie alla svolgimento della action e
alla successiva costruzione della risposta.

Nell'ambito di una action   possibile accedere al business process corrente tramite l'ActionContext,
mentre in una JSP mediante un metodo statico di BusinessProcess.
responsabilità  della JSP includere nella risposta il riferimento al business process in cui
dovrà  essere valutata la prossima action.

La valutazione di una action può  portare alla creazione di un nuovo business process o alla chiusura
di quello corrente. Nel caso di un nuovo business process è possibile aggiungerlo come figlio del
business process corrente. In tal caso le ricerche di forward che non portano ad un risultato immediato,
procedono nella gerarchia dei padri prima di passare alla mappatura statica.
Un business process può essere restituito da una action come forward;
in tale caso verrà cercato il forward dal nome "default" nell'ambito del business process restituito.

Esiste sempre un business process che rappresenta la radice della gerarchia di tutti i business process.
Se non specificato dalla richiesta esso è il business process di default.

Un BusinessProcess non può  essere istanziato direttamente, ma solo tramite il metodo createBusinessProcess
dell'ActionContext, specificando il nome contenuto all'interno del file di configurazione della ActionServlet.
Subito dopo la creazione di un BusinessProcess viene invocato il metodo init con i parametri specificati
nel file di configurazione.

**CRUDComponent**

Componente generica per effettuare operazioni di CRUD su una classe di OggettiBulk.
Una CRUDComponent offre i seguenti servizi:
Creazione, modifica e cancellazione su base dati di OggettiBulk persistenti;
Controllo di consistenza transazionale per le operazioni CRUD;
Validazioni standard per le operazioni di creazione e modifica: campi NOT_NULLABLE e lunghezza
massima di campi di testo;

Riempimento automatico di attributi "keys" e "options" specificati nei BulkInfo;
Gestione automatica di ricerche semplici (basate sui valori degli attributi persistenti di un OggettoBulk)
e ricerche complesse (specificate da alberi logici di clausole).
Gestione automatica di ricerche semplici e complesse per attributi secondari dell'oggetto principale.

## 👏 Come Contribuire 

Lo scopo principale di questo repository è di continuare a evolvere. Vogliamo contribuire a questo progetto nel modo più semplice e trasparente possibile e siamo grati alla comunità per ogni contribuito a correggere bug e miglioramenti.

## 📄 Licenza

JADA è distrubuito con licenza GNU AFFERO GENERAL PUBLIC LICENSE, che si trova nel file [LICENSE][l].

[l]: https://github.com/consiglionazionaledellericerche/jada/blob/master/LICENSE