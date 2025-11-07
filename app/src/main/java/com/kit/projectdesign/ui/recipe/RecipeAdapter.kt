package com.kit.projectdesign.ui.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kit.projectdesign.R
import com.kit.projectdesign.data.Recipe
import com.kit.projectdesign.databinding.ListItemRecipeBinding

class RecipeAdapter(
    private val recipes: List<Recipe>,
    private val onItemClicked: (Recipe) -> Unit,
    private val onLikeClicked: (Recipe) -> Unit // いいねクリック用のリスナーを追加
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = ListItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.bind(recipe)
    }

    override fun getItemCount() = recipes.size

    inner class RecipeViewHolder(private val binding: ListItemRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: Recipe) {
            binding.recipeImage.setImageResource(recipe.imageResId)
            binding.recipeName.text = recipe.name
            binding.recipeDescription.text = recipe.description

            // いいねの状態に応じてアイコンを切り替え
            val likeIcon = if (recipe.isLiked) R.drawable.ic_heart_filled else R.drawable.ic_heart_outline
            binding.likeButton.setImageResource(likeIcon)

            // クリックリスナーの設定
            // カード本体のクリック
            itemView.setOnClickListener {
                onItemClicked(recipe)
            }
            // いいねボタンのクリック
            binding.likeButton.setOnClickListener {
                onLikeClicked(recipe)
            }
        }
    }
}
