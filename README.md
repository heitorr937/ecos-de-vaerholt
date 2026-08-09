# Ecos de Vaerholt

Mod de terror psicológico para **Minecraft Forge 1.20.1**. Veja `LORE.md` para a história
completa.

## Importante: isto é código-fonte, não um .jar pronto

Mods de Minecraft (Forge, Fabric, etc.) **sempre** precisam ser compilados a partir do
código-fonte para virarem um `.jar` — isso vale pra qualquer mod, feito por mim ou por
qualquer desenvolvedor humano. A compilação usa o Gradle e precisa baixar peças do próprio
Minecraft e do Forge (que ficam nos servidores da Mojang/Forge). Eu não tenho acesso a esses
servidores no ambiente onde gero arquivos, então não consigo entregar o `.jar` já pronto
diretamente — só o projeto completo, pronto para compilar.

Tem duas formas de gerar o `.jar` a partir daqui. A opção A é a mais fácil (não precisa
instalar nada no seu PC).

---

## Opção A — Compilar automaticamente com GitHub Actions (recomendado)

Eu já incluí um workflow pronto em `.github/workflows/build.yml` que compila o mod pra você
na nuvem do GitHub (que tem acesso total à internet, ao contrário do meu ambiente).

1. Crie uma conta grátis no [github.com](https://github.com) se ainda não tiver.
2. Crie um repositório novo (pode ser privado), por exemplo `ecos-de-vaerholt`.
3. Suba o conteúdo desta pasta para esse repositório. Duas formas:
   - Pelo site: "Add file" → "Upload files", arraste tudo (incluindo a pasta `.github`,
     que às vezes fica escondida no seu gerenciador de arquivos — ative "mostrar arquivos
     ocultos" antes).
   - Ou pelo terminal, se tiver `git` instalado:
     ```
     cd vaerholt
     git init
     git add .
     git commit -m "primeiro commit"
     git branch -M main
     git remote add origin https://github.com/SEU_USUARIO/ecos-de-vaerholt.git
     git push -u origin main
     ```
4. Assim que o push terminar, vá na aba **Actions** do repositório no GitHub. Vai aparecer
   um workflow rodando ("Compilar mod (.jar)"). Leva uns 3-6 minutos.
5. Quando terminar (ícone verde ✅), clique nele → role até **Artifacts** → baixe
   `ecos-de-vaerholt-jar`. Dentro desse zip do GitHub está o `.jar` de verdade, pronto para
   colocar na pasta `mods` do seu Minecraft com Forge 1.20.1 instalado.

Se o build falhar (ícone vermelho ❌), clique nele para ver o log de erro — normalmente é
algo pequeno de API do Forge que precisa de um ajuste (comum em código gerado sem poder
compilar e testar antes). Pode colar o erro aqui pra mim que eu ajusto o código.

---

## Opção B — Compilar manualmente na sua máquina

1. Instale o **JDK 17**.
2. Baixe o **Forge MDK 1.20.1 - 47.2.20** em
   https://files.minecraftforge.net/net/minecraftforge/forge/index_1.20.1.html
   (escolha "Mdk" no menu de downloads da versão).
3. Extraia o MDK numa pasta nova.
4. Copie por cima (mesclando) as pastas `src/`, `.github/` e os arquivos `build.gradle`,
   `settings.gradle`, `gradle.properties` deste projeto para dentro da pasta do MDK,
   substituindo os arquivos de exemplo — **exceto** `gradlew`, `gradlew.bat` e a pasta
   `gradle/`, que devem continuar sendo os do MDK oficial.
5. No terminal, dentro da pasta:
   ```
   ./gradlew build          (Linux/Mac)
   gradlew.bat build        (Windows)
   ```
6. O `.jar` final aparece em `build/libs/`.

---


## Novidades desta versão (v1.1 — reformulação de qualidade)

- Texturas totalmente redesenhadas para as 3 criaturas originais (olhos brilhantes, bocas
  ocas, tecidos rasgados, pele cianótica) baseadas nas referências visuais que você mandou.
- **Ovos de spawn com arte própria** (não é mais o círculo de duas cores padrão do vanilla).
- **Nova criatura: O Enlutado** — stalker de terno e cartola que só observa à distância e
  desaparece quando encarado/aproximado. Puramente psicológico, nunca ataca.
- **Nova mecânica: possessão de animais** — vacas/porcos/ovelhas perto do jogador podem virar
  "Presas Possuídas" hostis quando o Pavor está alto. Nenhum animal é seguro perto de Vaerholt.
- **Jumpscare real**: em Pavor extremo, chance rara de um flash de cegueira + rugido alto e
  sincronizado — um susto de verdade, sem hitbox de mob, então sem risco de dano injusto.
- **Lanterna sem craft**: o jogador já começa com ela (e uma bateria reserva) no inventário.
- **Estrutura própria**: "Ruínas de Vaerholt" — pequenas câmaras de pedra em cavernas do
  overworld, cada uma com um baú que sorteia uma das 7 páginas de diário + itens de lore.
  A lore inteira só é reunida explorando várias ruínas diferentes.
- Partículas de poeira no feixe da lanterna para reforçar a sensação de luz física real.

## O que o mod já traz

- **Lanterna de Vaerholt**: luz dinâmica de verdade (não uma tocha disfarçada — reaproveita
  o bloco `minecraft:light` do próprio vanilla), raio curto e realista, dois modos
  (baixo/alto), bateria consumível, tremulação. Craft: ferro + Prata-Viva + Bateria de Piche.
- **Bateria de Piche**: craft com Prata-Viva + carvão; recarrega a lanterna.
- **Prata-Viva**: item de minério da lore; dropa dos Ceifados / pode ser adicionado a loot
  tables de cavernas (veja "Próximos passos").
- **Armário-Esconderijo**: bloco craftável (madeira escura); clique direito para se esconder
  e sumir da detecção do Descarnado, com pequena redução de Pavor.
- **Páginas de Diário**: 7 páginas com a lore completa em ordem; leem-se com clique direito,
  reduzem Pavor mas têm chance de picos súbitos.
- **Sussurrante**: entidade passiva-mas-perturbadora. Foge (teleporta) se encarada por muito
  tempo; se aproxima devagar se ignorada.
- **O Descarnado**: antagonista principal. Cego, caça por som/luz, anda devagar, dano
  moderado, sempre avisa com respiração antes de atacar.
- **Ceifado**: mineiro corrompido, ameaça de combate convencional (tipo zumbi reforçado).
- **Sistema de Pavor**: estatística oculta por jogador, sobe com escuridão/proximidade de
  entidades, desce com luz/esconderijo/tempo, efeitos visuais/sonoros por limiar.
- **Eventos aleatórios**: passos, batidas, sussurros, gritos distantes, mensagens falsas —
  frequência escala com o Pavor atual.
- **12 sons ambiente sintetizados** de verdade (não silêncio/placeholder): drone grave,
  sussurros, respiração, batimento cardíaco, passos, grito, estática, batidas, clique da
  lanterna.
- **3 modelos 3D próprios**, com animação procedural própria (sem reaproveitar humanoid
  vanilla), texturas proceduais inclusas.

## Próximos passos sugeridos (não incluídos ainda)

Coisas que dariam mais profundidade e que você (ou eu, se você quiser continuar depois)
pode adicionar:
- Um **Global Loot Modifier** injetando páginas de diário e Prata-Viva em loot tables de
  minas abandonadas vanilla, para descoberta orgânica da lore.
- Uma **estrutura customizada** (a vila de Vaerholt em ruínas) via `.nbt` de structure —
  precisa ser desenhada numa instância real do jogo com structure blocks.
- Camadas de textura mais elaboradas (ex.: emissive layer para os "olhos" ausentes do
  Descarnado, ou padrão de rachaduras na Prata-Viva).
- Um segundo estágio para o Sussurrante: se o jogador ignorá-lo por tempo demais em vez de
  encará-lo, ele poderia "grudar" em um comportamento mais assertivo — dando duas estratégias
  válidas de lidar com ele.
- Ajustar valores de balanceamento (dano, alcance de detecção, velocidade de drenagem de
  bateria) jogando bastante — os números que usei são um ponto de partida pensado, mas só
  playtesting real vai dizer se estão certos para o seu gosto de ritmo.

## Estrutura do projeto

```
src/main/java/com/vaerholt/echoes/
├── EchoesMod.java              ponto de entrada
├── registry/                   registro de itens, blocos, entidades, sons, atributos
├── item/                       Lanterna, Bateria, Diário
├── block/                      Armário-Esconderijo
├── entity/
│   ├── whisperer/               Sussurrante (IA de encarar/teleportar)
│   ├── flayed/                  Descarnado (IA de caça por som/luz)
│   └── ceifado/                 Ceifado (combate convencional)
├── client/
│   ├── model/                   3 modelos customizados (geometria + animação)
│   ├── renderer/                 renderers ligando modelo + textura
│   └── light/                    hack de luz dinâmica da lanterna
├── capability/                  sistema de Pavor por jogador
├── dread/                       tick de Pavor + ambiência
└── events/                      eventos aleatórios de terror

src/main/resources/
├── assets/echoes/sounds/        12 arquivos .ogg sintetizados
├── assets/echoes/textures/      texturas de item/entidade/bloco
├── assets/echoes/models,lang,blockstates,sounds.json
└── data/echoes/recipes,loot_tables
```
