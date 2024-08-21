package com.example.simplegames.ui.profileScreens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.simplegames.R
import com.example.simplegames.data.model.ShopCategory
import com.example.simplegames.data.model.ShopItem
import com.example.simplegames.ui.theme.SimpleGamesTheme

@Composable
fun ProfileMainScreen(
    profileImage: Any,
    availableItems: List<ShopItem>,
    onNewImageLoad: (Uri) -> Unit,
    onFeatureClick: (ShopCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) {
        if (it != null) {
            onNewImageLoad(it)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.profile_top_indent)))
        AsyncImage(
            modifier = Modifier
                .size(dimensionResource(id = R.dimen.profile_image_size))
                .clip(CircleShape)
                .border(1.dp, Color.Gray, CircleShape)
                .clickable {
                    photoPicker.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                },
            model = ImageRequest.Builder(LocalContext.current)
                .data(profileImage)
                .crossfade(enable = true)
                .build(),
            contentDescription = stringResource(R.string.profile_image),
            contentScale = ContentScale.Crop,
        )
        FeaturesList(
            onFeatureClick = onFeatureClick,
            availableItems = availableItems
        )
    }
}

@Composable
fun FeaturesList(
    onFeatureClick: (ShopCategory) -> Unit,
    availableItems: List<ShopItem>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(availableItems) { feature ->
            FeatureCard(
                featureLabel = stringResource(id = feature.title),
                onClick = {
                    onFeatureClick(feature.category)
                }
            )
        }
    }
}

@Composable
fun FeatureCard(
    featureLabel: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
        ) {
            Text(
                text = featureLabel,
                style = MaterialTheme.typography.displayMedium
            )
        }
    }
}

@Preview
@Composable
fun ProfileMainScreenPreview() {
    SimpleGamesTheme {
        ProfileMainScreen(
            profileImage = R.drawable.default_profile,
            onNewImageLoad = {},
            availableItems = listOf(),
            onFeatureClick = {}
        )
    }
}