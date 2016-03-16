package es.ubiqua.zapptv.listeners;

public interface OnApplicationUpdatedListener {
	public void onUpdateRequired(boolean required);
	public void onDataDownloaded(boolean downloaded);
	public void onDataDecompressed(boolean decompressed);
}
