package isel.pdm.ee.battleship.users.author.ui

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import isel.pdm.ee.battleship.R

data class SocialInfo(val link: Uri, @DrawableRes val imageId: Int)

@Composable
fun UsersView(username : String, email : String,ic:Int?, modifier : Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        var icPicture = ic
        if (icPicture==null)
            icPicture = R.drawable.clipart3240117
        Image(
            painter = painterResource(id = icPicture),
            contentDescription = null,
            modifier = Modifier.sizeIn(100.dp, 100.dp, 200.dp, 200.dp)
        )
        Text(text = username, style = MaterialTheme.typography.titleLarge)
        Text(text = email, style = MaterialTheme.typography.titleLarge)
        Icon(
            imageVector = Icons.Default.Email,
            contentDescription = null,
            modifier = Modifier.sizeIn(50.dp, 50.dp, 100.dp, 100.dp)
        )
    }
}

@Composable
fun Socials(
    onOpenUrlRequested: (Uri) -> Unit = { },
    socials: Iterable<SocialInfo>
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.widthIn(min = 120.dp, max = 240.dp)
    ) {
        socials.forEach {
            Social(id = it.imageId, onClick = { onOpenUrlRequested(it.link) })
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun Social(@DrawableRes id: Int, onClick: () -> Unit) {
    Image(
        painter = painterResource(id = id),
        contentDescription = null,
        modifier = Modifier.clickable { onClick() }
    )
}
