using System.Text.Json.Serialization;

namespace App.Models;

public class LoginResponse
{
    [JsonPropertyName("sID")]
    public string? Token { get; set; }

    [JsonPropertyName("status")]
    public string? Status { get; set; }

    [JsonPropertyName("message")]
    public string? Message { get; set; }
}
