package gcera.app.eruduyuru.adapters


import android.annotation.SuppressLint
import android.net.Uri
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import gcera.app.eruduyuru.R
import m7mdra.com.htmlrecycler.adapter.DescriptionListAdapter
import m7mdra.com.htmlrecycler.adapter.ElementsAdapter
import m7mdra.com.htmlrecycler.adapter.ListAdapter
import m7mdra.com.htmlrecycler.elements.*
import m7mdra.com.htmlrecycler.htmlfiy
import m7mdra.com.htmlrecycler.viewholder.*

class DetailAdapter : ElementsAdapter() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onBindElement(holder: RecyclerView.ViewHolder, position: Int) {
        val element = elements[position]
        when (holder) {
            is ParagraphViewHolder -> {
                if (element !is UnknownElement) {
                    val paragraphElement=element as ParagraphElement
                    val text=HtmlCompat.fromHtml(paragraphElement.paragraph,0)
                    holder.paragraphText.text = text
                    holder.paragraphText.movementMethod = LinkMovementMethod.getInstance()
                }
            }
            is ImageViewHolder -> {
                val imageElement = element as ImageElement
                Glide.with(holder.image).load(imageElement.ImageUrl).into(holder.image)
            }

            is Heading1ViewHolder -> {
                val heading1Element = element as Heading1Element
                holder.headingView.text = heading1Element.text
                holder.headingView.movementMethod = LinkMovementMethod.getInstance()
            }
            is Heading2ViewHolder -> {
                val heading1Element = element as Heading2Element
                holder.headingView.text = heading1Element.text
                holder.headingView.movementMethod = LinkMovementMethod.getInstance()
            }
            is Heading3ViewHolder -> {

                val heading1Element = element as Heading3Element
                holder.headingView.text = heading1Element.text
                holder.headingView.movementMethod = LinkMovementMethod.getInstance()
            }
            is Heading4ViewHolder -> {

                val heading1Element = element as Heading4Element
                holder.headingView.movementMethod = LinkMovementMethod.getInstance()
            }
            is Heading5ViewHolder -> {
                val heading1Element = element as Heading5Element
                holder.headingView.text = heading1Element.text
                holder.headingView.movementMethod = LinkMovementMethod.getInstance()
            }
            is Heading6ViewHolder -> {
                val heading1Element = element as Heading6Element
                holder.headingView.text = heading1Element.text
                holder.headingView.movementMethod = LinkMovementMethod.getInstance()
            }
            is AnchorLinkViewHolder -> {
                val anchorLinkElement = element as AnchorLinkElement
                val anchorUrl = anchorLinkElement.anchorUrl
                holder.anchorTextView.text = anchorUrl.displayText
                holder.anchorTextView.movementMethod = LinkMovementMethod.getInstance()
            }

            is BlockQuoteViewHolder -> {
                holder.blockQuoteTextView.text = (element as BlockQuoteElement).text
                holder.blockQuoteTextView.movementMethod = LinkMovementMethod.getInstance()
            }
            is OrderedListViewHolder -> {
                holder.recyclerView.apply {
                    val itemList = (element as OrderListElement).list
                    adapter = ListAdapter(itemList.first, itemList.second)
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                }
            }
            is UnOrderedListViewHolder -> {
                holder.recyclerView.apply {
                    val itemList = (element as UnOrderListElement).list
                    adapter = ListAdapter(itemList.first, itemList.second)
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                }
            }
            is DescriptionListViewHolder -> {
                holder.recyclerView.apply {
                    val descriptionList = (element as DescriptionListElement).descriptionList
                    adapter = DescriptionListAdapter(descriptionList)
                    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                }
            }
            is VideoViewHolder -> {
                holder.videoView.apply {
                    val videoElement = element as VideoElement
                    val parse = Uri.parse(videoElement.videoSourceUrl)
                    setVideoURI(parse)
                    start()
                }
            }
            is AudioViewHolder -> {
                holder.videoView.apply {
                    val parse = Uri.parse((element as AudioElement).audioSourceUrl)
                    setVideoURI(parse)
                    start()
                }
            }
            is IFrameViewHolder -> {
                holder.iframeView.loadUrl((element as IFrameElement).url)
                holder.iframeView.settings.javaScriptEnabled = true
            }
            is UnknownViewHolder->{
                holder.unknownTextView.text= htmlfiy((element as UnknownElement).html)
            }
        }
    }

    override fun onCreateElement(
        parent: ViewGroup, elementType: ElementType
    ): RecyclerView.ViewHolder {
        return when (elementType) {
            ElementType.Heading1 ->
                Heading1ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_heading1, parent, false))
            ElementType.Heading2 ->
                Heading2ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_heading2, parent, false))
            ElementType.Heading3 ->
                Heading3ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_heading3, parent, false))
            ElementType.Heading4 ->
                Heading4ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_heading4, parent, false))
            ElementType.Heading5 ->
                Heading5ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_heading5, parent, false))
            ElementType.Heading6 ->
                Heading6ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_heading6, parent, false))
            ElementType.IFrame ->
                IFrameViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_iframe, parent, false))
            ElementType.Image ->
                ImageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_image, parent, false))
            ElementType.Video ->
                VideoViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_video, parent, false))
            ElementType.Audio ->
                AudioViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_audio, parent, false))
            ElementType.Paragraph ->
                ParagraphViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_paragarph, parent, false))
            ElementType.Div ->
                DivViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_paragarph, parent, false))
            ElementType.BlockQuote ->
                BlockQuoteViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_blockquote, parent, false))
            ElementType.Unknown ->
                UnknownViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_unknown, parent, false))
            ElementType.AnchorLink ->
                AnchorLinkViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_anchor_link, parent, false))
            ElementType.OrderedList ->
                OrderedListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_list, parent, false))
            ElementType.UnorderedList ->
                UnOrderedListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_list, parent, false))
            ElementType.DescriptionList ->
                DescriptionListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_list, parent, false))
            else ->
                UnknownViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.row_unknown, parent, false))
        }
    }
}

