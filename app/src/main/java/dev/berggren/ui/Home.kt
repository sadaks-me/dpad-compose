package dev.berggren.ui

import android.util.Size
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import dev.berggren.ui.menu.MenuViewModel
import dev.berggren.ui.navigation.Screen
import dev.berggren.util.sectionMap

@Composable
fun Home() {
    val viewModel = hiltViewModel<MenuViewModel>()

    Rails(sectionMap) { viewModel.navigator.push(Screen.Empty.route) }

}

data class SectionModel(
    val name: String,
    val items: List<RailItem>,
    val size: Size
)

data class Category(
    val name: String,
    override val imageUrl: String,
) : RailItem()

data class Trainer(
    val id: String?,
    val name: String,
    override val imageUrl: String,
) : RailItem()

data class Workout(
    val id: String,
    val name: String,
    override val imageUrl: String,
    override val videoUrl: String
) : RailItem()

data class Practice(
    val id: String,
    val name: String,
    override val imageUrl: String,
    override val videoUrl: String
) : RailItem()

data class Programme(
    val id: String,
    val name: String,
    override val imageUrl: String
) : RailItem()

open class RailItem {
    open val imageUrl: String = ""
    open val videoUrl: String = ""
}

fun lessonImageLink(lessonId: String): String {
    return "https://vod-source-videos.s3.eu-west-2.amazonaws.com/lesson_images/$lessonId.jpg"
}

fun workoutImageLink(workoutId: String): String {
    return "https://vod-source-videos.s3.eu-west-2.amazonaws.com/workout-images/$workoutId.jpg"
}

fun trainerHeroImageLink(trainerId: String): String {
    return "https://vod-source-videos.s3.eu-west-2.amazonaws.com/trainer-images/hero/$trainerId.jpg"
}

fun trainerHeadshotImageLink(trainerId: String): String {
    return "https://vod-source-videos.s3.eu-west-2.amazonaws.com/trainer-images/headshots/$trainerId.jpg"
}

fun programmeImageLink(programmeId: String): String {
    return "https://vod-source-videos.s3.eu-west-2.amazonaws.com/programme-images/$programmeId.jpg"
}

fun categoryImageLink(categoryName: String, extension: String = ".png"): String {
    return "https://vod-source-videos.s3.eu-west-2.amazonaws.com/category-images/$categoryName$extension"
}

fun heroImageLink(imageName: String, extension: String = ".png"): String {
    return "https://vod-source-videos.s3.eu-west-2.amazonaws.com/hero-images/$imageName$extension"
}