package com.shagii.brandsnmobile;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private Context context;
    private List<Item> items;

    public PostAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        final Item item = items.get(position);
        holder.postTitel.setText(item.getTitle());

        //its hack to import Image from HTML Code Proper Method is Different the Below one
       /* Pattern p = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
        Matcher m = p.matcher(item.getContent());
        List<String> tokens = new ArrayList<>();
        while (m.find()) {
            String token = m.group(1);
            tokens.add(token);
        }
        Glide.with(context).load(tokens.get(0)).into(holder.postImage);
        */

        //Proper method of implementing Image and Text Description properly below

        Document document = Jsoup.parse(item.getContent());
        holder.postDescription.setText(document.text());

        //Elements Object creation for receving Images
        Elements elements = document.select("img");
       /* //This way we impelementing our image source
        Log.d("CODE", "Image - "+elements.get(0).attr("src"));
        //This way we implementing our text source
        Log.d("TEXT", document.text());*/

        Glide.with(context).load(elements.get(0).attr("src")).into(holder.postImage);

        //Here make OnClick Listner event to pass it in Detailed WebView Layout it binds on RecyclerView itemView
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("url", item.getUrl());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {
        ImageView postImage;
        TextView postTitel;
        TextView postDescription;

        public PostViewHolder(View itemView) {
            super(itemView);
            postImage = itemView.findViewById(R.id.postImage);
            postTitel = itemView.findViewById(R.id.postTitle);
            postDescription = itemView.findViewById(R.id.postDescription);
        }
    }
}
