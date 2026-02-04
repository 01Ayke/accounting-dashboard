using System;
using System.Globalization;
using System.Windows.Data;

namespace App.Helpers.Converters
{
    public class HeightMultiplierConverter : IValueConverter
    {
        public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
        {
            if (value is double height && double.TryParse(parameter?.ToString(), out double factor))
            {
                return height * factor;
            }
            return value;
        }

        public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
        {
            throw new NotImplementedException();
        }
    }
}