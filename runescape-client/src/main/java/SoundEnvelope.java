import net.runelite.mapping.Export;
import net.runelite.mapping.Implements;
import net.runelite.mapping.ObfuscatedName;
import net.runelite.mapping.ObfuscatedSignature;

@ObfuscatedName("bk")
@Implements("SoundEnvelope")
public class SoundEnvelope {
   @ObfuscatedName("at")
   @Export("segments")
   int segments = 2;
   @ObfuscatedName("an")
   @Export("durations")
   int[] durations = new int[2];
   @ObfuscatedName("av")
   @Export("phases")
   int[] phases = new int[2];
   @ObfuscatedName("as")
   @Export("start")
   int start;
   @ObfuscatedName("ax")
   @Export("end")
   int end;
   @ObfuscatedName("ap")
   @Export("form")
   int form;
   @ObfuscatedName("ab")
   @Export("ticks")
   int ticks;
   @ObfuscatedName("ak")
   @Export("phaseIndex")
   int phaseIndex;
   @ObfuscatedName("ae")
   @Export("step")
   int step;
   @ObfuscatedName("af")
   @Export("amplitude")
   int amplitude;
   @ObfuscatedName("ao")
   @Export("max")
   int max;

   SoundEnvelope() {
      this.durations[0] = 0;
      this.durations[1] = 65535;
      this.phases[0] = 0;
      this.phases[1] = 65535;
   }

   @ObfuscatedName("at")
   @ObfuscatedSignature(
      descriptor = "(Ltz;)V"
   )
   @Export("decode")
   final void decode(Buffer var1) {
      this.form = var1.readUnsignedByte();
      this.start = var1.readInt();
      this.end = var1.readInt();
      this.decodeSegments(var1);
   }

   @ObfuscatedName("an")
   @ObfuscatedSignature(
      descriptor = "(Ltz;)V"
   )
   @Export("decodeSegments")
   final void decodeSegments(Buffer var1) {
      this.segments = var1.readUnsignedByte();
      this.durations = new int[this.segments];
      this.phases = new int[this.segments];

      for(int var2 = 0; var2 < this.segments; ++var2) {
         this.durations[var2] = var1.readUnsignedShort();
         this.phases[var2] = var1.readUnsignedShort();
      }

   }

   @ObfuscatedName("av")
   @Export("reset")
   final void reset() {
      this.ticks = 0;
      this.phaseIndex = 0;
      this.step = 0;
      this.amplitude = 0;
      this.max = 0;
   }

   @ObfuscatedName("as")
   @Export("doStep")
   final int doStep(int var1) {
      if (this.max >= this.ticks) {
         this.amplitude = this.phases[this.phaseIndex++] << 15;
         if (this.phaseIndex >= this.segments) {
            this.phaseIndex = this.segments - 1;
         }

         this.ticks = (int)((double)this.durations[this.phaseIndex] / 65536.0 * (double)var1);
         if (this.ticks > this.max) {
            this.step = ((this.phases[this.phaseIndex] << 15) - this.amplitude) / (this.ticks - this.max);
         }
      }

      this.amplitude += this.step;
      ++this.max;
      return this.amplitude - this.step >> 15;
   }
}
