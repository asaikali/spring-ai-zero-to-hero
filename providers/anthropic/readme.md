# Anthropic 

To run the sample application you will need an Anthropic API key. The key
needs to be stored `src/main/resources/creds.yaml` file. 

If you have your own api key, copy the `src/main/resources/creds-template.
yaml` to `src/main/resources/creds.yaml` and add your key.

If you don't have your own key the instructor will provide you with `creds.yaml`
file you will need to put this file in `src/main/resources/creds.yaml`.

Run the application, it is better to run the app from the IDE so that you can 
put breakpoints.

## Anthorpic Does not Offer Embeddig Models

From the docs https://docs.anthropic.com/en/docs/build-with-claude/embeddings#how-to-get-embeddings-with-anthropic

> How to get embeddings with Anthropic
>
> Anthropic does not offer its own embedding model. One embeddings provider that has a wide variety of options and capabilities encompassing all of the above considerations is Voyage AI.
>
> Voyage AI makes state-of-the-art embedding models and offers customized models for specific industry domains such as finance and healthcare, or bespoke fine-tuned models for individual customers.
>
> The rest of this guide is for Voyage AI, but we encourage you to assess a variety of embeddings vendors to find the best fit for your specific use case.
