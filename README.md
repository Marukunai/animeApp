# animeApp

App Android nativa (Java) para consultar un catálogo de anime, guardar favoritos y reproducir episodios. Es el cliente móvil de [animeDB](https://github.com/Marukunai/animeDB), la API REST que expone los datos.

Proyecto académico (trabajo en grupo, curso 2DAM) usado también como pieza de portfolio.

## Stack

- **Java** nativo (sin Kotlin), `minSdk` 27 / `targetSdk` 34
- **Retrofit 2** + **Gson** — consumo de la API REST de `animeDB`
- **Glide** — carga de imágenes de los animes
- **WebView** — reproducción de enlaces embed (Mega.nz) que no son un archivo de vídeo directo
- **View Binding** para las pantallas basadas en `ViewBinding` (`MainActivity`)
- **Navigation Component** + **ViewModel/LiveData** para la sección con Drawer (Home / Gallery / Slideshow)
- `SharedPreferences` para sesión de usuario y preferencia de tema

## Estructura del proyecto

```
app/src/main/java/com/example/anime/
├── activity/       Activities de la app (Login, Register, Anime, Favoritos, Profile, EditarPerfil, Video, Home, Main)
├── adapters/       Adapters de RecyclerView (AnimeAdapter, VideoAdapter)
├── api/            Definición de la capa Retrofit (ApiClient, AnimeApiService, UsuarioApiService)
├── model/          POJOs que mapean las respuestas JSON de la API (Anime, Usuario, Video)
└── ui/
    ├── favoritos/  Fragment + adapter de favoritos (patrón alternativo con RecyclerView propio)
    ├── home/       Fragment + ViewModel (plantilla base de Android Studio)
    ├── gallery/    Fragment + ViewModel (plantilla base de Android Studio)
    ├── slideshow/  Fragment + ViewModel (plantilla base de Android Studio)
    └── profile/    Fragment que reutiliza el layout de ProfileActivity
```

## Flujo de pantallas

`LoginActivity` (launcher) → `RegisterActivity` ↔ `LoginActivity` → tras login correcto → `MainActivity` (Drawer con Home/Gallery/Slideshow) o directamente a las Activities de contenido: `AnimeActivity` (detalle + episodios en `RecyclerView`) → `VideoActivity` (reproductor, con WebView para Mega.nz o VideoView nativo para el resto), y `ProfileActivity` → `FavoritosActivity` / `EditarPerfilActivity`.

## Arquitectura: por qué hay dos estilos distintos

El proyecto mezcla **dos patrones** dentro del mismo módulo, y merece la pena tenerlo identificado antes de una entrevista técnica:

1. **Activities "clásicas"** (`LoginActivity`, `RegisterActivity`, `AnimeActivity`, `FavoritosActivity`, `ProfileActivity`, `EditarPerfilActivity`, `VideoActivity`): hacen `findViewById` directo, llaman a Retrofit desde la propia Activity y actualizan la UI en el callback. Es donde vive toda la lógica real de la app (login, favoritos, vídeo, perfil).
2. **Fragments con ViewModel/LiveData** (`ui/home`, `ui/gallery`, `ui/slideshow`): es la plantilla "Navigation Drawer Activity" que genera Android Studio por defecto al crear el proyecto. Se mantuvo como esqueleto de navegación (`MainActivity`) pero su contenido nunca se sustituyó por datos reales — siguen mostrando el texto de ejemplo ("This is home fragment", etc.).
3. `ui/favoritos/FavoritosFragment` sí sigue el patrón Fragment pero sin ViewModel, llamando a Retrofit directamente — es una tercera variante a medio camino entre las dos anteriores.

Si te preguntan por arquitectura, la respuesta honesta y con criterio es: *"la navegación con Drawer viene de la plantilla de Android Studio; el contenido real de la app se implementó como Activities independientes porque cada pantalla necesitaba su propio ciclo de vida y llamada a red, y no se llegó a migrar todo a Fragments/ViewModel por tiempo"*.

## Cómo ejecutarlo

1. Levantar primero el backend [animeDB](https://github.com/Marukunai/animeDB) (ver su README) — la app no funciona sin la API corriendo.
2. Abrir la carpeta `animeApp` en Android Studio y dejar que sincronice Gradle.
3. `ApiClient.BASE_URL` ya está puesta a `http://10.0.2.2:8080/`, que es la dirección con la que el emulador de Android Studio ve el `localhost` de tu PC. Si pruebas en un dispositivo físico en la misma red, cámbiala por la IP local de tu PC (ej. `192.168.1.X`).
4. Ejecutar en un emulador o dispositivo con API 27+.

La sesión se guarda en `SharedPreferences` (`settings`: `isLoggedIn`, `userId`) tras un login correcto, y se limpia al pulsar "Cerrar sesión" en el perfil.

## Cosas a saber / posibles mejoras

Documentado aquí a propósito, para no encontrarlas en caliente delante de un entrevistador:

- **Favoritos por body/path vs. query params**: `insertarFavorito` envía un `@Body Map<String, Integer>` y `removeFavorite` usa variables de ruta (`{userId}/{animeId}`), pero el controlador del backend (`FavoritoController`) espera `@RequestParam` (parámetros de query) en ambos casos. Hay que alinear un lado con el otro para que añadir/quitar favoritos funcione de punta a punta.
- **`FavoritosActivity` lee el `userId` de una clave de `SharedPreferences` distinta** (`"user_session"`/`"user_id"`) a la que usa el resto de la app (`"settings"`/`"userId"`, ver `LoginActivity`/`ProfileActivity`) — con eso, esta pantalla concreta probablemente nunca encuentra el usuario logueado. `ui/favoritos/FavoritosFragment` sí usa la clave correcta.
- Las contraseñas se envían y comparan en texto plano (sin hashear) — aceptable para un proyecto académico, no para producción.
- Los Fragments de `ui/home`, `ui/gallery` y `ui/slideshow` siguen con el contenido de plantilla de Android Studio (no muestran datos reales).
- Hay dos implementaciones distintas de "lista de animes favoritos" (`adapters/AnimeAdapter` usado por `FavoritosActivity`, y `ui/favoritos/FavoritosAdapter` usado por `FavoritosFragment`) — podrían unificarse en una sola.

## Repos relacionados

- Backend: [animeDB](https://github.com/Marukunai/animeDB) — Spring Boot + MySQL
