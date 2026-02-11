using System.Globalization;
using System.Windows.Data;

namespace App.Helpers.Converters;

public class PercentageConverter : IValueConverter
{
    public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
    {
        double total = (double)value;
        double factor = System.Convert.ToDouble(parameter, CultureInfo.InvariantCulture);
        return total * factor;
    }

    public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
        => throw new NotImplementedException();
}
