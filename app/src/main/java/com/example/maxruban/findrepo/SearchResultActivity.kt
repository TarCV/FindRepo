package com.example.maxruban.findrepo

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.VisibleForTesting
import android.support.design.widget.Snackbar
import android.support.test.espresso.idling.CountingIdlingResource
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



open class SearchResultActivity : AppCompatActivity() {

//    val idlingResource = CountingIdlingResource("name1")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)

        val  searchTerm = intent.getStringExtra("searchTerm")
        val retriever = GitHubRetriever()

        if (searchTerm != null){
            //Search

            val callback = object : Callback<GitHubSearchResult> {
                override fun onResponse(call: Call<GitHubSearchResult>?, response: Response<GitHubSearchResult>?) {
                    val searchResult = response?.body()
                    if (searchResult != null) {
                        listRepos(searchResult!!.items)
                    }

//                    idlingResource.decrement()
                }

                override fun onFailure(call: Call<GitHubSearchResult>?, t: Throwable?) {
                    println("It is not working")
                }

            }
//            idlingResource.increment()
            retriever.searchRepos(callback,searchTerm)
        }else{
            // User Repo
            val username = intent.getStringExtra("username")
            val callback = object : Callback<List<Repo>> {
                override fun onResponse(call: Call<List<Repo>>?, response: Response<List<Repo>>?) {
                    if (response?.code() == 404){
                        println("User doesn't exist!")
                        val theView = this@SearchResultActivity.findViewById<View>(android.R.id.content)
                        Snackbar.make(theView, "User not found :( Go back and try again!", Snackbar.LENGTH_LONG).show()
                    }else {
                        val repos = response?.body()
                        if (repos != null) {
                            listRepos(repos)
                        }
                    }
                }
                override fun onFailure(call: Call<List<Repo>>?, t: Throwable?) {
                    println("Not working")

                }
            }
            retriever.userRepos(callback,username)
//            idlingResource.decrement()

        }
    }
    fun listRepos(repos : List<Repo>){
        val listView = findViewById<ListView>(R.id.repoListView)
        listView.setOnItemClickListener { adapterView, view, i, l ->
            val selectedRepo = repos!![i]
            //OPEN URL In A BROWSER
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(selectedRepo.html_url))
            startActivity(intent)
        }
        val adapter = RepoAdapter(this@SearchResultActivity, android.R.layout.simple_list_item_1, repos!!)
        listView.adapter = adapter

    }

/*
    open fun getEspressoIdlingResourceForSearchActivity(): CountingIdlingResource {
        return idlingResource
    }
*/
}

class RepoAdapter(context: Context?, resource: Int, objects: List<Repo>?): ArrayAdapter<Repo>(context, resource, objects){
    override fun getCount(): Int {
        return super.getCount()

    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var inflator = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val repoView = inflator.inflate(R.layout.repo_list_layout, parent, false)

        var textView = repoView.findViewById<TextView>(R.id.repoTextView)
        val imageView = repoView.findViewById<ImageView>(R.id.repoImageView)
        val repo = getItem(position)

        Picasso.get().load(Uri.parse(repo.owner.avatar_url)).into(imageView);
        textView.text = repo.full_name
        return repoView
    }

}