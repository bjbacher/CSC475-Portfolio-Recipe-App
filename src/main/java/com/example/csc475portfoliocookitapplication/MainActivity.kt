// CSC475 Portfolio CookIt Recipe Application
// Author: Brandon Bacher
package com.example.csc475portfoliocookitapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.csc475portfoliocookitapplication.ui.theme.CSC475PortfolioCookItApplicationTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    // API service for network requests
    private lateinit var apiService: ApiService

    // Adapter for the RecyclerView
    private lateinit var recipeAdapter: RecipeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the API service using Retrofit
        apiService = ApiService.create()

        // Initialize the adapter with an empty list of recipes
        recipeAdapter = RecipeAdapter(listOf())

        // Set the content using Jetpack Compose
        setContent {
            CSC475PortfolioCookItApplicationTheme {
                // Surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")

                    // Set up RecyclerView within the Compose layout
                    AndroidView(
                        factory = { context ->
                            // Create RecyclerView and set its layout manager and adapter
                            RecyclerView(context).apply {
                                layoutManager = LinearLayoutManager(context)
                                adapter = recipeAdapter
                            }
                        },
                        update = { recyclerView ->
                            // Launch a coroutine to fetch recipes from the API
                            lifecycleScope.launch {
                                try {
                                    // Request to get recipes
                                    val recipes = apiService.getRecipes()

                                    // Update the adapter with the fetched recipes
                                    recipeAdapter.updateRecipes(recipes)
                                } catch (e: Exception) {
                                    // Handle any errors encountered during the API call
                                    e.printStackTrace()
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CSC475PortfolioCookItApplicationTheme {
        Greeting("Android")
    }
}
