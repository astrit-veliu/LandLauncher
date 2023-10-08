package io.github.astrit_veliu.landlauncher.ui.presentation.onboarding

import io.github.astrit_veliu.landlauncher.R

class OnBoardingItems(
    val image: Int,
    val title: Int,
    val desc: Int
) {
    companion object{
        fun getData(): List<OnBoardingItems>{
            return listOf(
                OnBoardingItems(R.drawable.ic_welcome_placeholder, R.string.on_board_title_1, R.string.on_board_description_1),
                OnBoardingItems(R.drawable.ic_drawer_placeholder, R.string.on_board_title_2, R.string.on_board_description_2),
                OnBoardingItems(R.drawable.ic_personalize_placeholder, R.string.on_board_title_3, R.string.on_board_description_3)
            )
        }
    }
}
