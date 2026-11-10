package echo.music.iad1tya.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.palette.graphics.Palette
import coil3.compose.rememberAsyncImagePainter
import coil3.imageLoader
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.request.crossfade
import coil3.toBitmap
import echo.music.iad1tya.R
import echo.music.iad1tya.models.MediaMetadata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun rememberAdjustedFontSize(
  text: String,
  maxWidth: Dp,
  maxHeight: Dp,
  density: Density,
  initialFontSize: TextUnit = 20.sp,
  minFontSize: TextUnit = 14.sp,
  style: TextStyle = TextStyle.Default,
  textMeasurer: androidx.compose.ui.text.TextMeasurer? = null
): TextUnit {
  val measurer = textMeasurer ?: rememberTextMeasurer()

  var calculatedFontSize by
    remember(text, maxWidth, maxHeight, style, density) {
      val initialSize =
        when {
          text.length < 50 -> initialFontSize
          text.length < 100 -> (initialFontSize.value * 0.8f).sp
          text.length < 200 -> (initialFontSize.value * 0.6f).sp
          else -> (initialFontSize.value * 0.5f).sp
        }
      mutableStateOf(initialSize)
    }

  LaunchedEffect(key1 = text, key2 = maxWidth, key3 = maxHeight) {
    val targetWidthPx = with(density) { maxWidth.toPx() * 0.92f }
    val targetHeightPx = with(density) { maxHeight.toPx() * 0.92f }
    if (text.isBlank()) {
      calculatedFontSize = minFontSize
      return@LaunchedEffect
    }

    if (text.length < 20) {
      val largerSize = (initialFontSize.value * 1.1f).sp
      val result =
        measurer.measure(text = AnnotatedString(text), style = style.copy(fontSize = largerSize))
      if (result.size.width <= targetWidthPx && result.size.height <= targetHeightPx) {
        calculatedFontSize = largerSize
        return@LaunchedEffect
      }
    } else if (text.length < 30) {
      val largerSize = (initialFontSize.value * 0.9f).sp
      val result =
        measurer.measure(text = AnnotatedString(text), style = style.copy(fontSize = largerSize))
      if (result.size.width <= targetWidthPx && result.size.height <= targetHeightPx) {
        calculatedFontSize = largerSize
        return@LaunchedEffect
      }
    }

    var minSize = minFontSize.value
    var maxSize = initialFontSize.value
    var bestFit = minSize
    var iterations = 0

    while (minSize <= maxSize && iterations < 20) {
      iterations++
      val midSize = (minSize + maxSize) / 2
      val midSizeSp = midSize.sp

      val result =
        measurer.measure(text = AnnotatedString(text), style = style.copy(fontSize = midSizeSp))

      if (result.size.width <= targetWidthPx && result.size.height <= targetHeightPx) {
        bestFit = midSize
        minSize = midSize + 0.5f
      } else {
        maxSize = midSize - 0.5f
      }
    }

    calculatedFontSize = if (bestFit < minFontSize.value) minFontSize else bestFit.sp
  }

  return calculatedFontSize
}

enum class LyricsBackgroundStyle {
  SOLID,
  BLUR,
  GRADIENT
}

enum class LyricsCardAspectRatio(val label: String) {
  SQUARE_1_1("1:1 Square"),
  STORY_9_16("9:16 Story (Instagram)")
}

enum class LyricsThemePreset(val label: String) {
  DYNAMIC("Dynamic"),
  SPOTIFY("Spotify"),
  AURORA("Aurora"),
  AMOLED("AMOLED"),
  SUNSET("Sunset"),
  OCEAN("Ocean"),
  VINTAGE("Vintage")
}

object LyricsCardThemes {
  val SpotifyBackground = Color(0xFF121212)
  val SpotifyAccent = Color(0xFF1DB954)
  val SpotifySecondary = Color(0xB3FFFFFF)

  val AuroraGradient = listOf(Color(0xFF6B21A8), Color(0xFF1E1B4B), Color(0xFF06B6D4))
  val AuroraText = Color.White
  val AuroraSecondary = Color.White.copy(alpha = 0.75f)

  val AmoleDarkBackground = Color(0xFF000000)
  val AmoleDarkText = Color.White
  val AmoleDarkSecondary = Color(0xFF888888)

  val SunsetGradient = listOf(Color(0xFFE11D48), Color(0xFF9333EA), Color(0xFFF59E0B))
  val SunsetText = Color.White
  val SunsetSecondary = Color.White.copy(alpha = 0.8f)

  val OceanGradient = listOf(Color(0xFF0F172A), Color(0xFF1E3A8A), Color(0xFF0284C7))
  val OceanText = Color(0xFFE0F2FE)
  val OceanSecondary = Color(0xFF7DD3FC)

  val VintageBackground = Color(0xFF292524)
  val VintageText = Color(0xFFFEF3C7)
  val VintageSecondary = Color(0xFFD6D3D1)
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun LyricsImageCard(
  lyricText: String,
  mediaMetadata: MediaMetadata,
  darkBackground: Boolean = true,
  backgroundColor: Color? = null,
  backgroundStyle: LyricsBackgroundStyle = LyricsBackgroundStyle.SOLID,
  textColor: Color? = null,
  secondaryTextColor: Color? = null,
  textAlign: TextAlign = TextAlign.Center,
  aspectRatio: LyricsCardAspectRatio = LyricsCardAspectRatio.SQUARE_1_1,
  customGradient: List<Color>? = null
) {
  val context = LocalContext.current
  val density = LocalDensity.current

  val cardCornerRadius = 20.dp
  val padding = if (aspectRatio == LyricsCardAspectRatio.STORY_9_16) 24.dp else 28.dp
  val coverArtSize = if (aspectRatio == LyricsCardAspectRatio.STORY_9_16) 72.dp else 64.dp

  val defaultBgColor = if (darkBackground) Color(0xFF121212) else Color(0xFFF5F5F5)
  val backgroundSolidColor = backgroundColor ?: defaultBgColor

  val mainTextColor = textColor ?: if (darkBackground) Color.White else Color.Black
  val secondaryColor =
    secondaryTextColor
      ?: if (darkBackground) Color.White.copy(alpha = 0.7f) else Color.Black.copy(alpha = 0.7f)

  val painter =
    rememberAsyncImagePainter(
      ImageRequest.Builder(context).data(mediaMetadata.thumbnailUrl).crossfade(false).build()
    )

  var gradientBrush by remember { mutableStateOf<Brush?>(null) }

  if (backgroundStyle == LyricsBackgroundStyle.GRADIENT) {
    if (customGradient != null && customGradient.size >= 2) {
      gradientBrush = Brush.linearGradient(colors = customGradient, tileMode = TileMode.Clamp)
    } else {
      LaunchedEffect(mediaMetadata.thumbnailUrl) {
        withContext(Dispatchers.IO) {
          try {
            val loader = context.imageLoader
            val req =
              ImageRequest.Builder(context)
                .data(mediaMetadata.thumbnailUrl)
                .allowHardware(false)
                .build()
            val result = loader.execute(req)
            val bmp = result.image?.toBitmap()
            if (bmp != null) {
              val palette = Palette.from(bmp).generate()
              val vibrant = palette.getVibrantColor(defaultBgColor.toArgb())
              val muted = palette.getMutedColor(defaultBgColor.toArgb())
              val darkVibrant = palette.getDarkVibrantColor(defaultBgColor.toArgb())

              val color1 = Color(vibrant)
              val color2 = Color(darkVibrant)

              gradientBrush =
                Brush.linearGradient(colors = listOf(color1, color2), tileMode = TileMode.Clamp)
            }
          } catch (_: Exception) {}
        }
      }
    }
  }

  Box(
    modifier = Modifier.background(Color.Black).fillMaxSize(),
    contentAlignment = Alignment.Center
  ) {
    Box(modifier = Modifier.fillMaxSize()) {
      when (backgroundStyle) {
        LyricsBackgroundStyle.SOLID -> {
          Box(modifier = Modifier.fillMaxSize().background(backgroundSolidColor))
        }
        LyricsBackgroundStyle.BLUR -> {
          Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().blur(50.dp).background(Color.Black.copy(alpha = 0.3f))
          )
        }
        LyricsBackgroundStyle.GRADIENT -> {
          Box(
            modifier =
              Modifier.fillMaxSize()
                .background(
                  gradientBrush
                    ?: androidx.compose.ui.graphics.Brush.linearGradient(
                      listOf(backgroundSolidColor, backgroundSolidColor)
                    )
                )
          )
        }
      }
    }

    Box(modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(cardCornerRadius))) {
      when (backgroundStyle) {
        LyricsBackgroundStyle.SOLID -> {
          Box(modifier = Modifier.fillMaxSize().background(backgroundSolidColor))
        }
        LyricsBackgroundStyle.BLUR -> {

          Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().blur(50.dp).background(Color.Black.copy(alpha = 0.3f))
          )
        }
        LyricsBackgroundStyle.GRADIENT -> {
          Box(
            modifier =
              Modifier.fillMaxSize()
                .background(
                  gradientBrush
                    ?: androidx.compose.ui.graphics.Brush.linearGradient(
                      listOf(backgroundSolidColor, backgroundSolidColor)
                    )
                )
          )
        }
      }

      Box(
        modifier =
          Modifier.fillMaxSize()
            .border(1.dp, mainTextColor.copy(alpha = 0.09f), RoundedCornerShape(cardCornerRadius))
      )

      Column(
        modifier = Modifier.fillMaxSize().padding(padding),
        verticalArrangement = Arrangement.SpaceBetween
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
        ) {
          Image(
            painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier =
              Modifier.size(coverArtSize)
                .clip(RoundedCornerShape(3.dp))
                .border(1.dp, mainTextColor.copy(alpha = 0.16f), RoundedCornerShape(3.dp))
          )
          Spacer(modifier = Modifier.width(16.dp))
          Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.weight(1f)
          ) {
            Text(
              text = mediaMetadata.title,
              color = mainTextColor,
              fontSize = 20.sp,
              fontWeight = FontWeight.Bold,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
              modifier = Modifier.padding(bottom = 2.dp)
            )
            Text(
              text = mediaMetadata.artists.joinToString { it.name },
              color = secondaryColor,
              fontSize = 16.sp,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }
        }

        BoxWithConstraints(
          modifier = Modifier.weight(1f).fillMaxWidth().padding(vertical = 6.dp),
          contentAlignment =
            when (textAlign) {
              TextAlign.Left,
              TextAlign.Start -> Alignment.CenterStart
              TextAlign.Right,
              TextAlign.End -> Alignment.CenterEnd
              else -> Alignment.Center
            }
        ) {
          val availableWidth = maxWidth
          val availableHeight = maxHeight
          val textStyle =
            TextStyle(
              color = mainTextColor,
              fontWeight = FontWeight.Bold,
              textAlign = textAlign,
              letterSpacing = 0.005.em,
            )

          val textMeasurer = rememberTextMeasurer()
          val initialSize =
            when {
              lyricText.length < 50 -> 24.sp
              lyricText.length < 100 -> 20.sp
              lyricText.length < 200 -> 17.sp
              lyricText.length < 300 -> 15.sp
              else -> 13.sp
            }

          val dynamicFontSize =
            rememberAdjustedFontSize(
              text = lyricText,
              maxWidth = availableWidth - 8.dp,
              maxHeight = availableHeight - 8.dp,
              density = density,
              initialFontSize = initialSize,
              minFontSize = 18.sp,
              style = textStyle,
              textMeasurer = textMeasurer
            )

          Text(
            text = lyricText,
            style =
              textStyle.copy(
                fontSize = dynamicFontSize,
                lineHeight = dynamicFontSize.value.sp * 1.2f
              ),
            overflow = TextOverflow.Ellipsis,
            textAlign = textAlign,
            modifier = Modifier.fillMaxWidth()
          )
        }

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
          Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier.size(22.dp)
          )

          Spacer(modifier = Modifier.width(8.dp))

          Text(
            text = context.getString(R.string.app_name),
            color = secondaryColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }
  }
}
