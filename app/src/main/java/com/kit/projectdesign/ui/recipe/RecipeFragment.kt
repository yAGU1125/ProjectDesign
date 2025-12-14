package com.kit.projectdesign.ui.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.kit.projectdesign.data.Recipe
import com.kit.projectdesign.databinding.FragmentRecipeBinding

class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecipeViewModel by viewModels()
    private lateinit var recipeAdapter: RecipeAdapter
    // We don't need a local mutable list if we use the list from ViewModel, 
    // but the adapter logic for "isLiked" might need to update the local object or ViewModel.
    // For simplicity, we'll let the adapter handle the list it gets.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initial setup of adapter with empty list
        recipeAdapter = RecipeAdapter(
            recipes = emptyList(), // Initialize with empty
            onItemClicked = { recipe ->
                val action = RecipeFragmentDirections.actionRecipeToDetail(recipe)
                findNavController().navigate(action)
            },
            onLikeClicked = { recipe ->
                handleLikeClick(recipe)
            }
        )
        binding.recyclerViewRecipe.adapter = recipeAdapter

        // Observe ViewModel
        viewModel.recipes.observe(viewLifecycleOwner) { recipes ->
            // Re-create adapter or update data. 
            // Ideally, add a method in Adapter to update data.
            // Here we re-instantiate for simplicity as per current pattern.
            recipeAdapter = RecipeAdapter(
                recipes = recipes,
                onItemClicked = { recipe ->
                    val action = RecipeFragmentDirections.actionRecipeToDetail(recipe)
                    findNavController().navigate(action)
                },
                onLikeClicked = { recipe ->
                    handleLikeClick(recipe)
                }
            )
            binding.recyclerViewRecipe.adapter = recipeAdapter
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            // Handle loading state (e.g., show progress bar)
            // binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Fetch data
        viewModel.fetchRecipes()
    }

    private fun handleLikeClick(clickedRecipe: Recipe) {
        // 1. いいねの状態を反転させる
        clickedRecipe.isLiked = !clickedRecipe.isLiked

        // 2. アダプターにデータの変更を通知する
        // Since we re-created adapter with the list from LiveData, we can notify change on that adapter.
        // However, we need to know the index.
        // Also, mutating the object directly inside the list held by adapter is a bit hacky but works for simple apps.
        // A better way would be to update ViewModel.
        
        // For now, let's just notify the adapter if we can find the index.
        // But since we don't have a direct reference to the list *inside* the adapter (unless we expose it),
        // we might just notify the whole item changed if we knew the position.
        // The previous implementation used `recipeList.indexOf(clickedRecipe)`.
        
        // Let's rely on the fact that we passed the list to the adapter.
        // If we want to support this correctly with the backend, we should probably send a request to API.
        // For this task (adding backend for importing content), local toggle is fine.
        
        // Use `notifyDataSetChanged` for simplicity or find index if possible.
        binding.recyclerViewRecipe.adapter?.notifyDataSetChanged()

        // 3. いいねされた場合にSnackbarでメッセージを表示
        if (clickedRecipe.isLiked) {
            val message = "「${clickedRecipe.name}」はいいねしたレシピに保存されました。"
            Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
                .setDuration(3000)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
