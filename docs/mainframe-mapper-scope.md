# Mainframe mapper: accordi e attività rimandate

Decisioni concordate il 16 novembre 2026.

## Prima versione

- Generare nei setter la validazione di base comune: lunghezza delle stringhe e limiti numerici ricavabili dalla copybook, con eccezione in caso di violazione. Ulteriori vincoli verranno definiti successivamente.
- Un MainframeMapper separato dai DTO e dal client CTG/ECI, con encode(dto) e decodeInto(output, dto).
- Riutilizzare i converter e i serializer LegStar esistenti.
- Gestire i gruppi ricorsivamente, anche quando il tipo Java è una classe separata.
- Rendere esplicito l'ordine dei campi generati, incluso il riferimento ai gruppi nel padre.
- Stesso layout COBOL per richiesta e risposta.
- Aggiornare direttamente il DTO originale tramite setter. Un'eccezione può lasciare aggiornamenti parziali: il chiamante deve considerare inutilizzabile il DTO dopo l'errore. Non sono richiesti rollback o aggiornamenti atomici.
- Verificare la lunghezza del buffer rispetto al layout. I setter applicano la validazione di base anche ai valori ricevuti.
- Prevedere default per padding e code page, configurabili tramite plugin e utilizzabili dal mapper.
- Accompagnare gli errori con il percorso del campo e il vincolo violato.

## Rimandato esplicitamente

- Vincoli di validazione aggiuntivi oltre alla validazione di base condivisa.
- Sistema aggiuntivo di verifica delle annotation generate (duplicati, assenze e controlli estesi del layout); mantenere soltanto i controlli necessari al funzionamento.
- Test con buffer reali di riferimento del mainframe. Questo rinvio non esclude i normali controlli di compilazione e le verifiche mirate delle modifiche.
- Nuovo supporto specifico nel mapper per FILLER, OCCURS e REDEFINES. Non rimuovere il supporto già esistente in LegStar.

## Punti di integrazione individuati

- legstar-generator: generatore bean e template JTE per DTO, annotation e setter.
- legstar-maven-plugin: BeanGeneratorMojo per esporre le opzioni di generazione.
- legstar-base: annotation e supporto condiviso ai DTO.
- legstar-converter: mapper, metadati e conversione dei buffer.
- CobolConverterConfigBase contiene già code page e opzioni di padding: riutilizzarle dove possibile.

## Aggiornamento: validazione OCCURS

Implementati nei setter: dimensione esatta per OCCURS fisso, intervallo min/max per DEPENDING ON, validazione di base degli elementi stringa e numerici con indice nel messaggio. Null resta ammesso. La coerenza con il campo contatore DEPENDING ON va controllata nel futuro mapper, quando il DTO è completo. Il supporto OCCURS nel mapper resta da implementare.

## Step 2: encode

MainframeMapper.encode(dto) produce il buffer grezzo con serializer LegStar per chiamata. Configurazione tramite CobolConverterConfig (default EBCDIC); esposizione delle opzioni dal plugin da completare in un passo successivo. @CobolOrder viene generato su tutti i campi, inclusi gruppi e array. I vecchi DTO senza ordine conservano il comportamento precedente.
Gruppi e array devono essere inizializzati prima di encode; elementi null degli array sono rifiutati con indice. Controllo della dimensione OCCURS e del contatore DEPENDING ON prima della serializzazione. REDEFINES resta escluso dal nuovo mapper. decodeInto è il passo successivo.

## Step 3: decodeInto

decodeInto(output, dto) aggiorna tramite setter lo stesso DTO, preservando i gruppi esistenti e creando quelli null. Gli array vengono ricreati usando il contatore letto nella risposta, con controllo min/max e conversione degli elementi. Controllo dei byte disponibili prima di ogni campo primitivo e rifiuto dei byte residui. Le eccezioni vengono lanciate come IllegalArgumentException con percorso del campo e causa originale; il DTO può essere parzialmente aggiornato e deve essere scartato dopo un errore.

## Configurazione dal plugin e diagnostica encode

Il goal `generate-bean` accetta:

```xml
<configuration>
    <hostCharsetName>IBM01140</hostCharsetName>
    <rightPadCobolAlphanumWithSpaces>true</rightPadCobolAlphanumWithSpaces>
    <truncateHostStringsTrailingSpaces>false</truncateHostStringsTrailingSpaces>
</configuration>
```

Questi sono i default. Con padding `false` vengono aggiunti low values (byte zero), mantenendo la lunghezza COBOL. Il trim riguarda solo gli spazi finali in lettura. Il charset viene validato durante la generazione.

Il generatore bean scrive `@CobolEncoding` sulla classe radice. `new MainframeMapper()` usa tali impostazioni per encode e decodeInto; un DTO privo dell'annotation usa i default LegStar. `new MainframeMapper(config)` dà precedenza alla configurazione esplicita. Le opzioni sono esposte anche come proprietà Maven con prefisso `legstar.`.

Gli errori di conversione durante encode riportano il percorso Java del campo e l'indice degli array, conservando la catena delle cause. Esempio: `Request.items[2].amount`. La chiamata CTG/ECI rimane responsabilità del client.

## Flag validateSetters

Nel goal generate-bean, `<validateSetters>false</validateSetters>` disabilita tutti i controlli generati nei setter, compresi quelli sugli OCCURS e sui loro elementi. Il default è true; proprietà Maven `legstar.validateSetters`. Anche la configurazione Java espone `setValidateSetters(boolean)`. Il flag opera in fase di generazione: occorre rigenerare e ricompilare i DTO. I controlli strutturali del mapper (dimensione buffer, contatori OCCURS) restano attivi.

## Contratto Copybook

I DTO dei gruppi generati implementano `org.legstar.cobol.model.Copybook`, interfaccia marker pubblica in legstar-base. Le firme del mapper sono `encode(Copybook dto)` e `decodeInto(byte[] output, Copybook dto)`. Rigenerare i DTO esistenti oppure aggiungere esplicitamente implements Copybook ai DTO manuali. Le annotation continuano a definire il layout; la marker interface limita i tipi accettati a compilazione.
