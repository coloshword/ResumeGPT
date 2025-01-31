from pathlib import Path

def get_relative_path(relative_path: str):
    """
    Returns an absolute path relative to the script's location.
    Params:
        relative_path (str): The relative path (e.g., "folder1/folder2/file.csv" or "folder1\\folder2\\file.csv").

    Returns:
        Path object representing the absolute path.
    """
    return Path(__file__).parent / Path(relative_path)