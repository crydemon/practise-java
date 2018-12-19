package beam;

import java.util.Arrays;
import org.apache.beam.runners.direct.DirectRunner;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Count;
import org.apache.beam.sdk.transforms.FlatMapElements;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.TypeDescriptors;

public class WordCount {


  public static void main(String[] args) {
    PipelineOptions options = PipelineOptionsFactory.create();
    // 显式指定PipelineRunner：DirectRunner（Local模式）
    options.setRunner(DirectRunner.class);
    Pipeline p = Pipeline.create(options);
    p.apply(TextIO.read().from("d:\\wallet.txt"))
        .apply("ExtractWords", FlatMapElements
            .into(TypeDescriptors.strings())
            .via((String word) -> Arrays.asList(word.split(","))))
        .apply(Count.perElement())
        .apply("FormatResults", MapElements
            .into(TypeDescriptors.strings())
            .via(
                ((KV<String, Long> wordCount) -> wordCount.getKey() + ": " + wordCount.getValue())))
        .apply(TextIO.write().to("wordCounts"));
    p.run().waitUntilFinish();
  }
}
